(ns generative-design-clojure.01-P.P-1-2-3-04.P-1-2-3-04
  (:require [quil.core :as q]
            [quil.middleware :as m]
            [generative-design-clojure.util :as util]))

(def color-count 20)

(defn setup []
  (q/color-mode :hsb 360 100 100)
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
                              (q/random 20)))
        brightness-values (for [i (range color-count)]
                            (if (= (mod i 2) 0)
                              (q/random 100)
                              100))
        counter           (atom 0)
        row-count         (q/random 5 30)
        row-height        (/ (q/height) row-count)]
    (loop [i 0]
      (when (< i row-count)
        (let [part-count (atom (+ i 1))
              parts      (atom ())]
          (loop [ii 0]
            (when (< ii @part-count)
              (if (< (q/random 1) 0.075)
                (let [fragments (q/random 2 20)]
                  (swap! part-count + fragments)
                  (loop [iii 0]
                    (when (< iii fragments)
                      (swap! parts conj (q/random 2)))))
                (swap! parts conj (q/random 2 20)))
              (recur (inc ii))))
          (let [sum-parts-total (reduce + @parts)
                sum-parts-now   (atom 0)]
            (loop [ii 0]
              (when (< ii (count @parts))
                (swap! sum-parts-now + (nth @parts ii))
                (when (< (q/random 1.0) 0.45)
                  (let [index (mod @counter color-count)
                        x     (+ (q/map-range @sum-parts-now
                                              0 sum-parts-total
                                              0 (q/width))
                                 (q/random -10 10))
                        y     (+ (* row-height i)
                                 (q/random -10 10))
                        w     (+ (* (q/map-range (nth @parts ii)
                                                 0 sum-parts-total
                                                 0 (q/width))
                                    -1)
                                 (q/random -10 10))
                        h     (* row-height 1.5)]
                    (q/begin-shape)
                    (q/fill 0 0 0 180)
                    (q/vertex x y)
                    (q/vertex (+ x w) y)
                    (q/fill (nth hue-values index)
                            (nth saturation-values index)
                            (nth brightness-values index)
                            100)
                    (q/vertex (+ x w) (+ y h))
                    (q/vertex x (+ y h))
                    (q/end-shape :close)))
                (swap! counter inc)
                (recur (inc ii))))))
        (recur (inc i))))))

(defn mouse-released [state _]
  (assoc state :act-random-seed (q/random 100000)))

(q/defsketch P_1_2_3_04
  :middleware [m/fun-mode]
  :renderer :p3d
  :size [800 800]
  :setup setup
  :draw draw
  :key-pressed util/key-controller
  :mouse-released mouse-released)
