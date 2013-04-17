;; ======================================================================
;; this code is meant to be stepped through interactively, not
;; executed at once.  It demonstrates some basic features of
;; shadertone.  Kind of an "introductory tour"...
(ns demo0
  (:use [overtone.live]
        [overtone.synth.stringed])
  (:require [shadertone.tone :as t]))

;; ======================================================================
;; bring up a simple visualization.
;; - the red component is just a ramp
;; - the green component is based on the Overtone sound volume
;; - the blue component is a sinusoid based on the time.
(t/start "examples/simple.glsl")

;; ======================================================================
;; now define some sounds...
(def gtr (guitar))
(def snare (sample (freesound-path 26903)))
(def kick (sample (freesound-path 2086)))
(def close-hihat (sample (freesound-path 802)))
(def open-hihat (sample (freesound-path 26657)))
;; play the sounds. see how it affects the shader
;; some drums...
(snare)
(kick)
(close-hihat)
(open-hihat)
;; some guitar strums...
(guitar-strum gtr :E :down 1.25)
(guitar-strum gtr :A :up 0.25)
(guitar-strum gtr :G :down 0.5)
(guitar-strum gtr [-1 -1 -1 -1 -1 -1]) ; mute
;; maybe add some distortion
(ctl gtr
     :pre-amp 5.0 :distort 0.76
     :lp-freq 2000 :lp-rq 0.25
     :rvb-mix 0.5 :rvb-room 0.7 :rvb-damp 0.4)

;; ======================================================================
;; try some other visualizations...make sure you play with the sounds,
;; too.
;; see that you can easily go fullscreen
(t/start-fullscreen "examples/simple.glsl")
;; or you can control width & height
(t/start "examples/sine_dance.glsl"
         :width 800 :height 800
         :title "Sine Dance")
(t/start "examples/quasicrystal.glsl")
(t/start "examples/wave.glsl")

;; these testcase sounds are mainly for the fft & wave shader
;; (warning, a little loud)
(demo 5 (* 1.25 (sin-osc))) ;; looks like a max to me
(demo 15 (mix (sin-osc [(mouse-x 20 20000 EXP)
                        (mouse-y 20 20000 EXP)])))
(demo 10 (mix (sin-osc [100 1000])))  ; 10000])))
(demo 15 (saw (mouse-x 20 20000 EXP)))
(demo 15 (square (mouse-x 20 20000 EXP)))

;; ======================================================================
;; you can use textures, too.
;; use a keyword to tell where to place the waveform texture
;;   :iOvertoneAudio
(t/start "examples/simpletex.glsl"
         :textures [:iOvertoneAudio "textures/granite.png" "textures/towel.png"])
(t/start "examples/simpletexa.glsl"
         :title "Simple Tex w/Alpha"
         :textures [:iOvertoneAudio "textures/granite_alpha.png" "textures/towel.png"])
(t/start "examples/simplecube.glsl" :textures [:iOvertoneAudio "textures/buddha_*.jpg"])

;; ======================================================================
;; the user-data api.  create atoms and send them to your shader
;; NOTE your shader needs to define:
;;   uniform float iRGB;
;; to match the user-data map below
(def my-rgb (atom [0.3 0.1 0.5]))
(t/start "examples/rgb.glsl" :user-data { "iRGB" my-rgb})
;; now you can adjust your data at-will and it will be sent to
;; the GPU at 60Hz
(swap! my-rgb (fn [x] [0.55 0.95 0.75]))

;; stop the shader display
(t/stop)

;; ======================================================================
;; some less interesting basic tests of shadertoy...
(t/start "examples/calendar.glsl") ;; very slowly changes
(t/start "examples/mouse.glsl") ;; click-n-drag mouse