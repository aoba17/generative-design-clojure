(ns generative-design-clojure.01-P.P-4-3-1-01.P-4-3-1-01
  (:require [quil.core :as q]
            [quil.middleware :as m]
            [generative-design-clojure.util :as util]))

(def data-location "src/generative_design_clojure/01_P/P_4_3_1_01/data/")

(defn setup []
  (let [image (q/load-image (str data-location "pic.png"))]
    {:draw-mode 1
     :img       image}))

(defn draw [{:keys [draw-mode img]}]
  (q/background 255)
  (dotimes [grid-x (.-width img)]
    (dotimes [grid-y (.-height img)]
      (let [mouse-x-factor (q/map-range (q/mouse-x) 0 (q/width) 0.05 1)
            mouse-y-factor (q/map-range (q/mouse-y) 0 (q/height) 0.05 1)
            tile-width     (/ (q/width) (.-width img))
            tile-height    (/ (q/height) (.-height img))
            pos-x          (* tile-width grid-x)
            pos-y          (* tile-height grid-y)
            c              (q/get-pixel img grid-x grid-y)
            greyscale      (q/round (+ (* (q/red c) 0.222)
                                       (* (q/green c) 0.707)
                                       (* (q/blue c) 0.071)))]
        (case draw-mode
          1 (let [w1 (q/map-range greyscale 0 255 15 0.1)]
              (q/stroke 0)
              (q/stroke-weight (* w1 mouse-x-factor))
              (q/line pos-x pos-y (+ pos-x 5) (+ pos-y 5)))
          2 (let [r2 (* 1.1284
                        (q/sqrt (* tile-width
                                   tile-width
                                   (- 1 (/ greyscale 255))))
                        mouse-x-factor
                        3)]
              (q/ellipse pos-x pos-y r2 r2))
          3 (let [l3 (* (q/map-range greyscale 0 255 30 0.1)
                        mouse-x-factor)]
              (q/stroke 0)
              (q/stroke-weight (* 10 mouse-y-factor))
              (q/line pos-x pos-y (+ pos-x l3) (+ pos-y l3)))
          4 (let [w4 (q/map-range greyscale 0 255 10 0)
                  l4 (* (q/map-range greyscale 0 255 35 0)
                        mouse-y-factor)]
              (q/stroke 0)
              (q/stroke-weight (* w4
                                  mouse-x-factor
                                  0.1))
              (q/with-translation [pos-x pos-y]
                (q/rotate (* (/ greyscale 255)
                             q/PI))
                (q/line 0 0 l4 l4)))
          5 (let [w5         (q/map-range greyscale 0 255 5 0.2)
                  c2         (q/get-pixel img
                                          (min (+ grid-x 1)
                                               (- (.-width img) 1))
                                          grid-y)
                  h5         (* 50 mouse-x-factor)
                  greyscale2 (+ (* (q/red c2) 0.222)
                                (* (q/green c2) 0.707)
                                (* (q/blue c2) 0.071))
                  d1         (q/map-range greyscale 0 255 h5 0)
                  d2         (q/map-range greyscale2 0 255 h5 0)]
              (q/stroke-weight (+ (* w5
                                     mouse-y-factor)
                                  0.1))
              (q/stroke c2)
              (q/line (- pos-x d1)
                      (+ pos-y d1)
                      (- (+ pos-x tile-width) d2)
                      (+ pos-y d2)))
          6 (let [w6 (q/map-range greyscale 0 255 5 0.1)]
              (q/no-stroke)
              (q/fill c)
              (q/ellipse pos-x
                         pos-y
                         (* w6 mouse-x-factor)
                         (* w6 mouse-x-factor)))
          7 (let [w7 (q/map-range greyscale 0 255 5 0.1)]
              (q/stroke c)
              (q/stroke-weight w7)
              (q/fill 255 (* 255 mouse-x-factor))
              (q/with-translation [pos-x pos-y]
                (q/rotate (* (/ greyscale 255)
                             q/PI
                             mouse-y-factor))
                (q/rect 0 0 15 15)))
          8 (do
              (q/stroke 255 greyscale 0)
              (q/fill greyscale
                      (* greyscale mouse-x-factor)
                      (* 255 mouse-y-factor))
              (q/rect pos-x pos-y 3.5 3.5)
              (q/rect (+ pos-x 4) pos-y 3.5 3.5)
              (q/rect pos-x (+ pos-y 4) 3.5 3.5)
              (q/rect (+ pos-x 4) (+ pos-y 4) 3.5 3.5))
          9 (let [w9 (q/map-range greyscale 0 255 15 0.1)]
              (q/stroke 255 greyscale 0)
              (q/no-fill)
              (q/with-translation [pos-x pos-y]
                (q/rotate (* (/ greyscale 255) q/PI))
                (q/stroke-weight 1)
                (q/rect 0 0 (* 15 mouse-x-factor) (* 15 mouse-y-factor))
                (q/stroke-weight w9)
                (q/stroke 0 70)
                (q/ellipse 0 0 10 5))))))))

(defn key-released [state event]
  (case (:key event)
    :s (do (util/save-frame)
           state)
    :1 (assoc state :draw-mode 1)
    :2 (assoc state :draw-mode 2)
    :3 (assoc state :draw-mode 3)
    :4 (assoc state :draw-mode 4)
    :5 (assoc state :draw-mode 5)
    :6 (assoc state :draw-mode 6)
    :7 (assoc state :draw-mode 7)
    :8 (assoc state :draw-mode 8)
    :9 (assoc state :draw-mode 9)))

(q/defsketch P-4-3-1-01
  :middleware [m/fun-mode]
  :size [603 873]
  :setup setup
  :draw draw
  :key-released key-released
  :features [:resizable]
  :settings q/smooth)
