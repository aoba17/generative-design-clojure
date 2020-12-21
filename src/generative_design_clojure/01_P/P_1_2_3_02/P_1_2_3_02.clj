(ns generative-design-clojure.01-P.P-1-2-3-02.P-1-2-3-02
  (:require [quil.core :as q]
            [quil.middleware :as m]
            [generative-design-clojure.util :as util]))

(def color-count 20)

(defn setup []
  (q/color-mode :hsb 360 100 100 100)
  (q/no-stroke)
  {:hue-values        (for [i (range color-count)]
                        (if (= (mod i 2) 0)
                          (q/random 360)
                          195))
   :saturation-values (for [i (range color-count)]
                        (if (= (mod i 2) 0)
                          100
                          (q/random 100)))
   :brightness-values (for [i (range color-count)]
                        (if (= (mod i 2) 0)
                          (q/random 100)
                          100))})

(defn draw [{:keys [hue-values saturation-values brightness-values]}]
  (let [counter    (atom 0)
        row-count  (q/random 5 40)
        row-height (/ (q/height) row-count)]
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
                (let [index (mod @counter color-count)]
                  (q/fill (nth hue-values index)
                          (nth saturation-values index)
                          (nth brightness-values index))
                  (swap! sum-parts-now + (nth @parts ii))
                  (q/rect (q/map-range @sum-parts-now
                                       0 sum-parts-total
                                       0 (q/width))
                          (* row-height i)
                          (* (q/map-range (nth @parts ii)
                                          0 sum-parts-total
                                          0 (q/width))
                             -1)
                          row-height))
                (swap! counter inc)
                (recur (inc ii))))))
        (recur (inc i)))))
  (q/no-loop))

(defn mouse-released [state _]
  (q/start-loop)
  state)

(q/defsketch P_1_2_3_02
  :middleware [m/fun-mode]
  :size [800 800]
  :setup setup
  :draw draw
  :key-pressed util/key-controller
  :mouse-released mouse-released)
