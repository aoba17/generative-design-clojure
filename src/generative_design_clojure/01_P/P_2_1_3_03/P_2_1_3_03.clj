(ns generative-design-clojure.01-P.P-2-1-3-03.P-2-1-3-03
  (:require [quil.core :as q]
            [quil.middleware :as m]
            [generative-design-clojure.util :as util]))

(defn setup []
  (q/color-mode :hsb 360 100 100)
  (q/stroke 0)
  (q/no-fill)
  {:draw-mode    1
   :tile-count-x 6
   :tile-count-y 6})

(defn draw [{:keys [draw-mode
                    tile-count-x
                    tile-count-y]}]
  (q/background 360)
  (let [loop-count  (+ (/ (q/mouse-x) 20) 5)
        para        (- (/ (q/mouse-y) (q/height)) 0.5)
        tile-width  (/ (q/width) tile-count-x)
        tile-height (/ (q/height) tile-count-y)]
    (doseq [grid-y (range tile-count-y)]
      (doseq [grid-x (range tile-count-x)]
        (let [pos-x (+ (* tile-width grid-x)
                       (/ tile-width 2))
              pos-y (+ (* tile-height grid-y)
                       (/ tile-height 2))]
          (q/with-translation [pos-x pos-y]
            (case draw-mode
              1 (do
                  (q/translate (- (/ tile-width 2))
                               (- (/ tile-height 2)))
                  (doseq [i (range loop-count)]
                    (q/line 0
                            (* (+ para 0.5) tile-height)
                            tile-width
                            (* (/ tile-height loop-count) i))
                    (q/line 0
                            (* (/ tile-height loop-count) i)
                            tile-width
                            (- tile-height
                               (* (+ para 0.5)
                                  tile-height)))))
              2 (doseq [i (range loop-count)]
                  (q/line (* para tile-width)
                          (* para tile-height)
                          (/ tile-width 2)
                          (* (- (/ i loop-count) 0.5)
                             tile-height))
                  (q/line (* para tile-width)
                          (* para tile-height)
                          (/ tile-width -2)
                          (* (- (/ i loop-count) 0.5)
                             tile-height))
                  (q/line (* para tile-width)
                          (* para tile-height)
                          (* (- (/ i loop-count) 0.5)
                             tile-width)
                          (/ tile-height 2))
                  (q/line (* para tile-width)
                          (* para tile-height)
                          (* (- (/ i loop-count) 0.5)
                             tile-width)
                          (/ tile-height -2)))
              3 (doseq [i (range loop-count)]
                  (q/line 0
                          (* para tile-height)
                          (/ tile-width 2)
                          (* (- (/ i loop-count) 0.5)
                             tile-height))
                  (q/line 0
                          (* para tile-height)
                          (/ tile-width -2)
                          (* (- (/ i loop-count) 0.5)
                             tile-height))
                  (q/line 0
                          (* para tile-height)
                          (* (- (/ i loop-count) 0.5)
                             tile-width)
                          (/ tile-height 2))
                  (q/line 0
                          (* para tile-height)
                          (* (- (/ i loop-count) 0.5)
                             tile-width)
                          (/ tile-height -2))))))))))

(defn key-released [state event]
  (case (:key event)
    :1     (assoc state :draw-mode 1)
    :2     (assoc state :draw-mode 2)
    :3     (assoc state :draw-mode 3)
    :down  (update state :tile-count-y #(max (dec %) 1))
    :up    (update state :tile-count-y inc)
    :left  (update state :tile-count-x #(max (dec %) 1))
    :right (update state :tile-count-x inc)
    state))

(q/defsketch P-2-1-3-03
  :middleware [m/fun-mode]
  :size [600 600]
  :setup setup
  :draw draw
  :key-pressed util/key-controller
  :key-released key-released
  :settings q/smooth)
