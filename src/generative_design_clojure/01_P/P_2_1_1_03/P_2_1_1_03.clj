(ns generative-design-clojure.01-P.P-2-1-1-03.P-2-1-1-03
  (:require [quil.core :as q]
            [quil.middleware :as m]
            [generative-design-clojure.util :as util]))

(defn setup []
  (q/color-mode :hsb 360 100 100 100)
  (q/no-fill)
  {:color-back        [0 0 100]
   :color-left        [323 100 77]
   :color-right       [0 0 0]
   :transparent-left  false
   :transparent-right false
   :alpha-left        100
   :alpha-right       100
   :act-random-seed   0})

(defn draw [{:keys [color-back
                    color-left
                    color-right
                    transparent-left
                    transparent-right
                    act-random-seed]}]
  (q/random-seed act-random-seed)
  (q/stroke-weight (/ (q/mouse-x) 15))
  (let [[bh bs bb] color-back
        [lh ls lb] color-left
        [rh rs rb] color-right
        tile-count (/ (q/mouse-y) 15)]
    (q/background bh bs bb)

    (doseq [grid-y (range tile-count)]
      (doseq [grid-x (range tile-count)]
        (let [tile-size-x (/ (q/width) tile-count)
              tile-size-y (/ (q/height) tile-count)
              pos-x       (* tile-size-x grid-x)
              pos-y       (* tile-size-y grid-y)
              toggle      (q/floor (q/random 2))
              alpha-left  (if transparent-left
                            (* grid-y 10)
                            100)
              alpha-right (if transparent-right
                            (- 100 (* grid-y 10))
                            100)]
          (when (= toggle 0)
            (q/stroke lh ls lb alpha-left)
            (q/line pos-x
                    pos-y
                    (+ pos-x (/ tile-size-x 2))
                    (+ pos-y tile-size-y))
            (q/line (+ pos-x (/ tile-size-x 2))
                    pos-y
                    (+ pos-x tile-size-x)
                    (+ pos-y tile-size-y)))
          (when (= toggle 1)
            (q/stroke rh rs rb alpha-right)
            (q/line pos-x
                    (+ pos-y tile-size-y)
                    (+ pos-x (/ tile-size-x 2))
                    pos-y)
            (q/line (+ pos-x (/ tile-size-x 2))
                    (+ pos-y tile-size-y)
                    (+ pos-x tile-size-x)
                    pos-y)))))))

(defn mouse-pressed [state _]
  (assoc state :act-random-seed (q/random 100000)))

(defn key-released [{:keys [color-left
                            color-right]
                     :as   state}
                    event]
  (case (:key event)
    :1 (if (= color-left [273 73 51])
         (assoc state :color-left [323 100 77])
         (assoc state :color-left [273 73 51]))
    :2 (if (= color-right [0 0 0])
         (assoc state :color-right [192 100 64])
         (assoc state :color-right [0 0 0]))
    :3 (update state :transparent-left not)
    :4 (update state :transparent-right not)
    :0 (assoc state
              :transparent-left false
              :transparent-right false
              :color-left [323 100 77]
              :color-right [0 0 0])))

(q/defsketch P-2-1-1-03
  :middleware [m/fun-mode]
  :size [600 600]
  :setup setup
  :draw draw
  :mouse-pressed mouse-pressed
  :key-pressed util/key-controller
  :key-released key-released
  :settings q/smooth)
