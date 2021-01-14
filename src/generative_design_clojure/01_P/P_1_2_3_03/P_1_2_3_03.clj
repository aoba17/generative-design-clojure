(ns generative-design-clojure.01-P.P-1-2-3-03.P-1-2-3-03
  (:require [quil.core :as q]
            [quil.middleware :as m]
            [generative-design-clojure.util :as util]))

(def color-count 20)
(def alpha-value 27)

(defn setup []
  (q/color-mode :hsb 360 100 100 100)
  (q/no-stroke)
  {:act-random-seed 0})

(defn draw [{:keys [act-random-seed]}]
  (q/background 0 0 0)
  (q/random-seed act-random-seed)
  (let [hue-values        (for [i (range color-count)]
                            (if (= (mod i 2) 0)
                              (q/random 360)
                              195))
        saturation-values (for [i (range color-count)]
                            (if (= (mod i 2) 0)
                              100
                              (q/random 100)))
        brightness-values (for [i (range color-count)]
                            (if (= (mod i 2) 0)
                              (q/random 100)
                              100))
        counter           (atom 0)
        row-count         (q/random 5 30)
        row-height        (/ (q/height) row-count)]
    (doseq [i (range row-count)]
      (let [part-count (atom (+ i 1))
            parts      (atom ())]
        (doseq [_ (range @part-count)]
          (if (< (q/random 1) 0.075)
            (let [fragments (q/random 2 20)]
              (swap! part-count + fragments)
              (doseq [_ (range fragments)]
                (swap! parts conj (q/random 2))))
            (swap! parts conj (q/random 2 20))))
        (let [sum-parts-total (reduce + @parts)
              sum-parts-now   (atom 0)]
          (doseq [ii @parts]
            (swap! sum-parts-now + ii)
            (let [index (mod @counter color-count)
                  x     (q/map-range @sum-parts-now
                                     0 sum-parts-total
                                     0 (q/width))
                  y     (* row-height i)
                  w     (* (q/map-range ii
                                        0 sum-parts-total
                                        0 (q/width))
                           -1)
                  h     (* row-height 1.5)]
              (q/begin-shape)
              (q/fill 0 0 0)
              (q/vertex x y)
              (q/vertex (+ x w) y)
              (q/fill (nth hue-values index)
                      (nth saturation-values index)
                      (nth brightness-values index)
                      alpha-value)
              (q/vertex (+ x w) (+ y h))
              (q/vertex x (+ y h))
              (q/end-shape :close))
            (swap! counter inc)))))))

(defn mouse-released [state _]
  (assoc state :act-random-seed (q/random 100000)))

(q/defsketch P_1_2_3_03
  :middleware [m/fun-mode]
  :renderer :p3d
  :size [800 800]
  :setup setup
  :draw draw
  :key-pressed util/key-controller
  :mouse-released mouse-released)
