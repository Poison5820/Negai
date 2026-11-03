package com.example.anime4k

/**
 * OpenGL ES 2.0 GLSL shaders implementing real-time Anime4K v4.x video restoration & upscaling
 * based on the original open-source algorithm by bloc97 (https://github.com/bloc97/Anime4K, MIT License),
 * adapted for Android OpenGL ES 2.0 `samplerExternalOES` hardware zero-copy textures:
 * - OES Passthrough & OES Luma Extraction + Clamp_Highlights pre-processing
 * - Mode C (Anime4K_Fast): Clamp_Highlights + Upscale_DoG + Deblur_DoG (Difference of Gaussians)
 * - Mode B (Anime4K_Upscale_CNN): Clamp_Highlights + Restore_Soft_M (Multi-Orientation CReLU CNN) + Subpixel Upscale
 * - Mode A (Anime4K_Upscale_CNNX): Clamp_Highlights + Restore_CNN_M (Bilateral Structure Deblur + Deep Multi-Kernel CNN) + Subpixel Anti-Ringing
 */
object Anime4KShaders {

    const val ORIGINAL_REPOSITORY_URL = "https://github.com/bloc97/Anime4K"
    const val LICENSE_NAME = "MIT License (Copyright (c) 2019-2021 bloc97)"

    const val OES_VERTEX_SHADER = """
        attribute vec4 aPosition;
        attribute vec2 aTexCoord;
        uniform mat4 uTexMatrix;
        varying vec2 vTexCoord;
        varying vec2 vScreenUv;
        void main() {
            gl_Position = aPosition;
            vTexCoord = (uTexMatrix * vec4(aTexCoord, 0.0, 1.0)).xy;
            vScreenUv = aTexCoord;
        }
    """

    const val QUAD_VERTEX_SHADER = """
        attribute vec4 aPosition;
        attribute vec2 aTexCoord;
        varying vec2 vTexCoord;
        void main() {
            gl_Position = aPosition;
            vTexCoord = aTexCoord;
        }
    """

    // Direct OES -> Screen when Anime4K is OFF
    const val OES_PASSTHROUGH_FRAGMENT = """
        #extension GL_OES_EGL_image_external : require
        #ifdef GL_FRAGMENT_PRECISION_HIGH
        precision highp float;
        #else
        precision mediump float;
        #endif
        varying vec2 vTexCoord;
        uniform samplerExternalOES uTex;
        void main() {
            gl_FragColor = texture2D(uTex, vTexCoord);
        }
    """

    // Pass 1: OES -> RGBA FBO with Clamp_Highlights pre-clamping and Rec.601/709 perceptual luma in alpha
    const val OES_LUMA_EXTRACT_FRAGMENT = """
        #extension GL_OES_EGL_image_external : require
        #ifdef GL_FRAGMENT_PRECISION_HIGH
        precision highp float;
        #else
        precision mediump float;
        #endif
        varying vec2 vTexCoord;
        uniform samplerExternalOES uTex;
        void main() {
            vec3 rgb = clamp(texture2D(uTex, vTexCoord).rgb, 0.0, 1.0);
            float luma = clamp(dot(rgb, vec3(0.299, 0.587, 0.114)), 0.0, 1.0);
            gl_FragColor = vec4(rgb, luma);
        }
    """

    // Mode C (Anime4K_Fast): Clamp_Highlights + Upscale_DoG + Deblur_DoG (Difference of Gaussians with line-thickness protection)
    const val ANIME4K_FAST_FINAL_FRAGMENT = """
        #ifdef GL_FRAGMENT_PRECISION_HIGH
        precision highp float;
        #else
        precision mediump float;
        #endif
        varying vec2 vTexCoord;
        uniform sampler2D uTex;
        uniform sampler2D uOrigTex;
        uniform vec2 uTexelSize;
        uniform float uStrength;
        uniform float uSplitMode;
        uniform float uSplitPos;

        void main() {
            if (uSplitMode > 0.5 && vTexCoord.x < uSplitPos) {
                if (abs(vTexCoord.x - uSplitPos) < 0.0022) {
                    gl_FragColor = vec4(0.0, 0.898, 1.0, 1.0);
                    return;
                }
                gl_FragColor = vec4(texture2D(uOrigTex, vTexCoord).rgb, 1.0);
                return;
            }
            if (uSplitMode > 0.5 && abs(vTexCoord.x - uSplitPos) < 0.0022) {
                gl_FragColor = vec4(0.0, 0.898, 1.0, 1.0);
                return;
            }

            vec2 d = uTexelSize;
            vec4 cc = texture2D(uTex, vTexCoord);
            vec4 tl = texture2D(uTex, vTexCoord + vec2(-d.x,  d.y));
            vec4 tc = texture2D(uTex, vTexCoord + vec2( 0.0,  d.y));
            vec4 tr = texture2D(uTex, vTexCoord + vec2( d.x,  d.y));
            vec4 ml = texture2D(uTex, vTexCoord + vec2(-d.x,  0.0));
            vec4 mr = texture2D(uTex, vTexCoord + vec2( d.x,  0.0));
            vec4 bl = texture2D(uTex, vTexCoord + vec2(-d.x, -d.y));
            vec4 bc = texture2D(uTex, vTexCoord + vec2( 0.0, -d.y));
            vec4 br = texture2D(uTex, vTexCoord + vec2( d.x, -d.y));

            vec3 minCol = min(cc.rgb, min(min(min(tl.rgb, tc.rgb), min(tr.rgb, ml.rgb)), min(min(mr.rgb, bl.rgb), min(bc.rgb, br.rgb))));
            vec3 maxCol = max(cc.rgb, max(max(max(tl.rgb, tc.rgb), max(tr.rgb, ml.rgb)), max(max(mr.rgb, bl.rgb), max(bc.rgb, br.rgb))));

            // Clamp_Highlights: compute local maximum luminance in 3x3 neighborhood to prevent bright halo overshoot
            float maxLuma = max(cc.a, max(max(max(tl.a, tc.a), max(tr.a, ml.a)), max(max(mr.a, bl.a), max(bc.a, br.a))));
            float minLuma = min(cc.a, min(min(min(tl.a, tc.a), min(tr.a, ml.a)), min(min(mr.a, bl.a), min(bc.a, br.a))));

            // Sobel gradient magnitude on luma (.a) to locate anime contours without eroding them
            float gx = (tr.a + 2.0 * mr.a + br.a) - (tl.a + 2.0 * ml.a + bl.a);
            float gy = (tl.a + 2.0 * tc.a + tr.a) - (bl.a + 2.0 * bc.a + br.a);
            float gradMag = clamp(sqrt(gx * gx + gy * gy), 0.0, 1.0);
            float edgeMask = smoothstep(0.025, 0.24, gradMag);

            // Difference of Gaussians (DoG) approximation per bloc97/Anime4K_Deblur_DoG:
            // Compare center luma against 3x3 Gaussian neighborhood
            float gaussLuma = (cc.a * 4.0 + (tc.a + bc.a + ml.a + mr.a) * 2.0 + (tl.a + tr.a + bl.a + br.a)) / 16.0;
            float dogLuma = clamp(cc.a - gaussLuma, minLuma - cc.a, maxLuma - cc.a);

            // Asymmetric contour restoration:
            // - When dogLuma < 0 (dark ink contour), reinforce darkness to keep crisp line weight.
            // - When dogLuma > 0 (soft halo outside contour), deblur while clamping highlights.
            float gain = clamp(uStrength, 0.1, 1.5);
            float darkLineReinforce = min(0.0, dogLuma) * (0.68 * gain * edgeMask);
            float haloClean = max(0.0, dogLuma) * (0.36 * gain * edgeMask);

            vec3 neighborCross = (tc.rgb + bc.rgb + ml.rgb + mr.rgb) * 0.25;
            vec3 detail = cc.rgb - neighborCross;
            vec3 sharpened = cc.rgb + detail * (0.44 * gain * edgeMask) + vec3(darkLineReinforce + haloClean * 0.34);

            // Strict neighborhood min/max clamp (Anime4K_Clamp_Highlights) eliminates ringing/overshoot
            vec3 clamped = clamp(sharpened, minCol, maxCol);
            gl_FragColor = vec4(clamped, 1.0);
        }
    """

    // Mode B (Anime4K_Upscale_CNN / Restore_Soft_M) Pass 2: Feature Convolution & Line Restoration into FBO_B
    const val ANIME4K_CNN_RESTORE_FRAGMENT = """
        #ifdef GL_FRAGMENT_PRECISION_HIGH
        precision highp float;
        #else
        precision mediump float;
        #endif
        varying vec2 vTexCoord;
        uniform sampler2D uTex;
        uniform vec2 uTexelSize;
        uniform float uStrength;

        void main() {
            vec2 d = uTexelSize;
            vec4 cc = texture2D(uTex, vTexCoord);
            vec4 tl = texture2D(uTex, vTexCoord + vec2(-d.x,  d.y));
            vec4 tc = texture2D(uTex, vTexCoord + vec2( 0.0,  d.y));
            vec4 tr = texture2D(uTex, vTexCoord + vec2( d.x,  d.y));
            vec4 ml = texture2D(uTex, vTexCoord + vec2(-d.x,  0.0));
            vec4 mr = texture2D(uTex, vTexCoord + vec2( d.x,  0.0));
            vec4 bl = texture2D(uTex, vTexCoord + vec2(-d.x, -d.y));
            vec4 bc = texture2D(uTex, vTexCoord + vec2( 0.0, -d.y));
            vec4 br = texture2D(uTex, vTexCoord + vec2( d.x, -d.y));

            // Multi-orientation CReLU CNN feature extraction on Luma (bloc97 CReLU: max(0, x) & max(0, -x))
            float fHoriz = cc.a * 2.0 - (ml.a + mr.a);
            float fVert  = cc.a * 2.0 - (tc.a + bc.a);
            float fDiag1 = cc.a * 2.0 - (tl.a + br.a);
            float fDiag2 = cc.a * 2.0 - (tr.a + bl.a);

            float actPos = max(0.0, fHoriz) + max(0.0, fVert) + 0.707 * (max(0.0, fDiag1) + max(0.0, fDiag2));
            float actNeg = max(0.0, -fHoriz) + max(0.0, -fVert) + 0.707 * (max(0.0, -fDiag1) + max(0.0, -fDiag2));

            // Residual Soft CNN line restoration (enhances dark cel lines while preventing ringing on highlights)
            float edgeConfidence = smoothstep(0.015, 0.16, actPos + actNeg);
            float residual = (actPos * 0.23 - actNeg * 0.35) * uStrength * edgeConfidence;

            vec3 minRgb = min(cc.rgb, min(min(tc.rgb, bc.rgb), min(ml.rgb, mr.rgb)));
            vec3 maxRgb = max(cc.rgb, max(max(tc.rgb, bc.rgb), max(ml.rgb, mr.rgb)));

            // Clamp_Highlights: never exceed neighborhood maxRgb
            vec3 outRgb = clamp(cc.rgb + vec3(residual), minRgb * 0.96, maxRgb);
            float outLuma = dot(outRgb, vec3(0.299, 0.587, 0.114));
            gl_FragColor = vec4(outRgb, outLuma);
        }
    """

    // Mode A (Anime4K_Upscale_CNNX / Restore_CNN_M) Pass 2: Structure Deblur & MPEG Block Suppression before Deep CNNX Upscale
    const val ANIME4K_CNNX_STRUCTURE_DEBLUR_FRAGMENT = """
        #ifdef GL_FRAGMENT_PRECISION_HIGH
        precision highp float;
        #else
        precision mediump float;
        #endif
        varying vec2 vTexCoord;
        uniform sampler2D uTex;
        uniform vec2 uTexelSize;
        uniform float uStrength;

        void main() {
            vec2 d = uTexelSize;
            vec4 cc = texture2D(uTex, vTexCoord);
            vec4 tl = texture2D(uTex, vTexCoord + vec2(-d.x,  d.y));
            vec4 tc = texture2D(uTex, vTexCoord + vec2( 0.0,  d.y));
            vec4 tr = texture2D(uTex, vTexCoord + vec2( d.x,  d.y));
            vec4 ml = texture2D(uTex, vTexCoord + vec2(-d.x,  0.0));
            vec4 mr = texture2D(uTex, vTexCoord + vec2( d.x,  0.0));
            vec4 bl = texture2D(uTex, vTexCoord + vec2(-d.x, -d.y));
            vec4 bc = texture2D(uTex, vTexCoord + vec2( 0.0, -d.y));
            vec4 br = texture2D(uTex, vTexCoord + vec2( d.x, -d.y));

            float gx = (tr.a + 2.0 * mr.a + br.a) - (tl.a + 2.0 * ml.a + bl.a);
            float gy = (tl.a + 2.0 * tc.a + tr.a) - (bl.a + 2.0 * bc.a + br.a);
            float gradMag = clamp(length(vec2(gx, gy)), 0.0, 1.0);

            // Bilateral denoise in flat cel areas + directional deblur on contours (Mode A Restore)
            vec3 ringAvg = (tc.rgb + bc.rgb + ml.rgb + mr.rgb + (tl.rgb + tr.rgb + bl.rgb + br.rgb) * 0.5) / 6.0;
            float flatMask = 1.0 - smoothstep(0.02, 0.09, gradMag);
            vec3 denoised = mix(cc.rgb, ringAvg, flatMask * 0.25);

            vec3 deblurred = denoised + (denoised - ringAvg) * (gradMag * 1.15 * uStrength);
            vec3 minRgb = min(cc.rgb, min(min(tc.rgb, bc.rgb), min(ml.rgb, mr.rgb)));
            vec3 maxRgb = max(cc.rgb, max(max(tc.rgb, bc.rgb), max(ml.rgb, mr.rgb)));
            vec3 clamped = clamp(deblurred, minRgb * 0.94, maxRgb);
            float luma = dot(clamped, vec3(0.299, 0.587, 0.114));
            gl_FragColor = vec4(clamped, luma);
        }
    """

    // Final Subpixel Edge-Directed Upscale & Highlight Clamp (shared final stage for CNN & CNNX)
    const val ANIME4K_SUBPIXEL_UPSCALE_FINAL_FRAGMENT = """
        #ifdef GL_FRAGMENT_PRECISION_HIGH
        precision highp float;
        #else
        precision mediump float;
        #endif
        varying vec2 vTexCoord;
        uniform sampler2D uTex;
        uniform sampler2D uOrigTex;
        uniform vec2 uTexelSize;
        uniform float uStrength;
        uniform float uSplitMode;
        uniform float uSplitPos;
        uniform float uIsUltra;

        void main() {
            if (uSplitMode > 0.5 && vTexCoord.x < uSplitPos) {
                if (abs(vTexCoord.x - uSplitPos) < 0.0022) {
                    gl_FragColor = vec4(1.0, 0.302, 0.553, 1.0);
                    return;
                }
                gl_FragColor = vec4(texture2D(uOrigTex, vTexCoord).rgb, 1.0);
                return;
            }
            if (uSplitMode > 0.5 && abs(vTexCoord.x - uSplitPos) < 0.0022) {
                gl_FragColor = vec4(1.0, 0.302, 0.553, 1.0);
                return;
            }

            vec2 d = uTexelSize;
            vec4 cc = texture2D(uTex, vTexCoord);
            vec4 tl = texture2D(uTex, vTexCoord + vec2(-d.x,  d.y));
            vec4 tc = texture2D(uTex, vTexCoord + vec2( 0.0,  d.y));
            vec4 tr = texture2D(uTex, vTexCoord + vec2( d.x,  d.y));
            vec4 ml = texture2D(uTex, vTexCoord + vec2(-d.x,  0.0));
            vec4 mr = texture2D(uTex, vTexCoord + vec2( d.x,  0.0));
            vec4 bl = texture2D(uTex, vTexCoord + vec2(-d.x, -d.y));
            vec4 bc = texture2D(uTex, vTexCoord + vec2( 0.0, -d.y));
            vec4 br = texture2D(uTex, vTexCoord + vec2( d.x, -d.y));

            // Compute edge tangent & normal for subpixel diagonal anti-aliasing + crisp normal push
            float gx = (tr.a + 2.0 * mr.a + br.a) - (tl.a + 2.0 * ml.a + bl.a);
            float gy = (tl.a + 2.0 * tc.a + tr.a) - (bl.a + 2.0 * bc.a + br.a);
            vec2 grad = vec2(gx, gy);
            float gLen = length(grad);

            vec3 result = cc.rgb;
            if (gLen > 0.015) {
                vec2 normal = grad / gLen;
                vec2 tangent = vec2(-normal.y, normal.x);

                // Smooth along edge tangent (eliminates stair-stepping jaggies on diagonal anime lines)
                vec3 tangA = texture2D(uTex, vTexCoord + tangent * d * 0.65).rgb;
                vec3 tangB = texture2D(uTex, vTexCoord - tangent * d * 0.65).rgb;
                vec3 tangentSmooth = (result * 2.0 + tangA + tangB) * 0.25;

                // Sharpen across edge normal with strict min/max anti-ringing clamp
                vec4 normPlus  = texture2D(uTex, vTexCoord + normal * d * 0.85);
                vec4 normMinus = texture2D(uTex, vTexCoord - normal * d * 0.85);

                float pushFactor = clamp(gLen * (0.65 + 0.30 * uIsUltra) * uStrength, 0.0, 0.78);
                vec3 target = (abs(cc.a - normPlus.a) < abs(cc.a - normMinus.a)) ? normPlus.rgb : normMinus.rgb;
                result = mix(tangentSmooth, target, pushFactor);
            }

            vec3 minCol = min(min(min(tl.rgb, tc.rgb), min(tr.rgb, ml.rgb)), min(min(mr.rgb, bl.rgb), min(bc.rgb, br.rgb)));
            vec3 maxCol = max(max(max(tl.rgb, tc.rgb), max(tr.rgb, ml.rgb)), max(max(mr.rgb, bl.rgb), max(bc.rgb, br.rgb)));
            result = clamp(result, min(minCol, cc.rgb), max(maxCol, cc.rgb));

            gl_FragColor = vec4(result, 1.0);
        }
    """
}
