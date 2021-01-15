(ns generative-design-clojure.01-P.P-2-1-3-02.P-2-1-3-02
  (:require [quil.core :as q]
            [quil.middleware :as m]
            [generative-design-clojure.util :as util]))

(defn setup []
  (q/color-mode :hsb 360 100 100)
  {:draw-mode 1})

(defn draw [{:keys [draw-mode]}]
  (q/stroke-weight 0.5)
  (q/stroke-cap :round)

  (let [line-count   10
        tile-count-x (inc (/ (q/mouse-x) 30))
        tile-count-y (inc (/ (q/mouse-y) 30))
        tile-width   (/ (q/width) tile-count-x)
        tile-height  (/ (q/height) tile-count-y)
        x1           (/ tile-width 2)
        y1           (/ tile-height 2)
        x2           (atom 0)
        y2           (atom 0)
        line-weight  (atom 0)
        stroke-color (atom 0)]
    (case draw-mode
      1 (q/background 360)
      2 (q/background 360)
      3 (q/background 0))
    (doseq [grid-y (range tile-count-y)]
      (doseq [grid-x (range tile-count-x)]
        (q/with-translation [(* tile-width grid-x)
                             (* tile-height grid-y)]
          (doseq [side (range 4)]
            (doseq [i (range line-count)]
              (case side
                0 (do (swap! x2 + (/ tile-width line-count))
                      (reset! y2 0))
                1 (do (reset! x2 tile-width)
                      (swap! y2 + (/ tile-height line-count)))
                2 (do (swap! x2 - (/ tile-width line-count))
                      (reset! y2 tile-height))
                3 (do (reset! x2 0)
                      (swap! y2 - (/ tile-height line-count))))
              (if (< i (/ line-count 2))
                (do (swap! line-weight inc)
                    (swap! stroke-color + 60))
                (do (swap! line-weight dec)
                    (swap! stroke-color - 60)))
              (case draw-mode
                1 (q/stroke 0)
                2 (do (q/stroke 0)
                      (q/stroke-weight @line-weight))
                3 (do (q/stroke @stroke-color)
                      (q/stroke-weight (/ (q/mouse-x) 100))))
              (q/line x1 y1 @x2 @y2))))))))

(defn key-released [state event]
  (case (:key event)
    :1 (assoc state :draw-mode 1)
    :2 (assoc state :draw-mode 2)
    :3 (assoc state :draw-mode 3)
    state))

(q/defsketch P-2-1-3-02
  :middleware [m/fun-mode]
  :size [600 600]
  :setup setup
  :draw draw
  :key-pressed util/key-controller
  :key-released key-released
  :settings q/smooth)
