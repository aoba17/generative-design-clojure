(ns generative-design-clojure.01-P.P-2-1-2-03.P-2-1-2-03
  (:require [quil.core :as q]
            [quil.middleware :as m]
            [generative-design-clojure.util :as util]))

(defn setup []
  {:module-color    (q/color 0)
   :module-alpha    180
   :act-random-seed 0
   :max-distance    500})

(defn draw [{:keys [module-color
                    module-alpha
                    act-random-seed
                    max-distance]}]
  (q/background 255)
  (q/no-fill)
  (q/random-seed act-random-seed)
  (q/stroke (q/red module-color)
            (q/green module-color)
            (q/blue module-color)
            module-alpha)
  (q/stroke-weight 3)

  (doseq [grid-y (take (/ (q/height) 25)
                       (iterate (partial + 25) 0))]
    (doseq [grid-x (take (/ (q/width) 25)
                         (iterate (partial + 25) 0))]
      (let [diameter (*  (/  (q/dist (q/mouse-x) (q/mouse-y)
                                     grid-x grid-y)
                             max-distance)
                         40)]
        (q/with-translation [grid-x grid-y (* diameter 5)]
          (q/rect 0 0 diameter diameter))))))

(defn mouse-pressed [state _]
  (assoc state :act-random-seed (q/random 100000)))

(q/defsketch P-2-1-2-03
  :renderer :p3d
  :middleware [m/fun-mode]
  :size [600 600]
  :setup setup
  :draw draw
  :key-pressed util/key-controller
  :mouse-pressed mouse-pressed
  :settings q/smooth)
