(ns generative-design-clojure.01-P.P-2-1-3-05.P-2-1-3-05
  (:require [quil.core :as q]
            [quil.middleware :as m]
            [generative-design-clojure.util :as util]))

(defn setup []
  (q/color-mode :hsb 360 100 100)
  (q/no-stroke)
  {:act-random-seed 0})

(defn draw [{:keys [act-random-seed]}]
  (q/background 360)
  (q/random-seed act-random-seed)
  (let [tile-count-x 10
        tile-count-y 10
        color-step   6
        end-size     (/ (q/mouse-x) 10)
        step-size    (/ (q/mouse-y) 10)
        tile-width   (/ (q/width) tile-count-x)
        tile-height  (/ (q/height) tile-count-y)]
    (q/with-translation [(/ tile-width 2) (/ tile-height 2)]
      (doseq [grid-y (range tile-count-y)]
        (doseq [grid-x (range tile-count-x)]
          (let [pos-x  (* tile-width grid-x)
                pos-y  (* tile-height grid-y)
                switch (q/floor (q/random 4))]
            (doseq [i (range step-size)]
              (q/fill (- 360 (* i color-step)))
              (let [diameter (q/map-range i
                                          0 step-size
                                          tile-width end-size)]
                (case switch
                  0 (q/ellipse (+ pos-x i) pos-y
                               diameter diameter)
                  1 (q/ellipse pos-x (+ pos-y i)
                               diameter diameter)
                  2 (q/ellipse (- pos-x i) pos-y
                               diameter diameter)
                  3 (q/ellipse pos-x (- pos-y i)
                               diameter diameter))))))))))

(defn mouse-pressed [state _]
  (assoc state :act-random-seed (q/random 100000)))

(q/defsketch P-2-1-3-05
  :middleware [m/fun-mode]
  :size [600 600]
  :setup setup
  :draw draw
  :key-pressed util/key-controller
  :mouse-pressed mouse-pressed
  :settings q/smooth)
