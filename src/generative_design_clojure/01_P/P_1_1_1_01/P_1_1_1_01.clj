(ns generative-design-clojure.01-P.P-1-1-1-01.P-1-1-1-01
  (:require [quil.core :as q]
            [quil.middleware :as m]
            [generative-design-clojure.util :as util]))

(defn setup []
  (q/background 0)
  (q/color-mode :hsb (q/width) (q/height) 100)
  (q/no-stroke))

(defn draw [_]
  (let [step-x (+ (q/mouse-x) 2)
        step-y (+ (q/mouse-y) 2)]
    (doseq [grid-y (take (/ (q/height) step-y)
                         (iterate (partial + step-y) 0))]
      (doseq [grid-x (take (/ (q/width) step-x)
                           (iterate (partial + step-x) 0))]
        (q/fill grid-x (- (q/height) grid-y) 100)
        (q/rect grid-x grid-y step-x step-y)))))

(q/defsketch P-1-1-1-01
  :middleware [m/fun-mode]
  :size [800 400]
  :setup setup
  :draw draw
  :key-pressed util/key-controller)
