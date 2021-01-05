(ns generative-design-clojure.01-P.P-2-1-1-02.P-2-1-1-02
  (:require [quil.core :as q]
            [quil.middleware :as m]
            [generative-design-clojure.util :as util]))

(def tile-count 20)

(defn setup []
  (q/color-mode :hsb 360 100 100 100)
  {:color-left      [197 0 123]
   :color-right     [87 35 129]
   :alpha-left      100
   :alpha-right     100
   :act-random-seed 0
   :act-stroke-cap  :round})

(defn draw [{:keys [color-left
                    color-right
                    alpha-left
                    alpha-right
                    act-random-seed
                    act-stroke-cap]}]
  (q/background 360)
  (q/no-fill)
  (q/stroke-cap act-stroke-cap)
  (q/random-seed act-random-seed)

  (let [[lh ls lb] color-left
        [rh rs rb] color-right]
    (loop [grid-y 0]
      (when (< grid-y tile-count)
        (loop [grid-x 0]
          (when (< grid-x tile-count)
            (let [tile-size-x (/ (q/width) tile-count)
                  tile-size-y (/ (q/height) tile-count)
                  pos-x       (* tile-size-x grid-x)
                  pos-y       (* tile-size-y grid-y)
                  toggle      (q/floor (q/random 2))]
              (when (= toggle 0)
                (q/stroke lh ls lb alpha-left)
                (q/stroke-weight (/ (q/mouse-x) 10))
                (q/line pos-x
                        pos-y
                        (+ pos-x tile-size-x)
                        (+ pos-y tile-size-y)))
              (when (= toggle 1)
                (q/stroke rh rs rb alpha-right)
                (q/stroke-weight (/ (q/mouse-y) 10))
                (q/line pos-x
                        (+ pos-y tile-size-y)
                        (+ pos-x tile-size-x)
                        pos-y)))
            (recur (inc grid-x))))
        (recur (inc grid-y))))))

(defn mouse-pressed [state _]
  (assoc state :act-random-seed (q/random 100000)))

(defn key-released [{:keys [color-left
                            color-right
                            alpha-left
                            alpha-right]
                     :as   state}
                    event]
  (case (:key event)
    :1 (assoc state :act-stroke-cap :round)
    :2 (assoc state :act-stroke-cap :square)
    :3 (assoc state :act-stroke-cap :project)
    :4 (if (= color-left [0 0 0])
         (assoc state :color-left [323 100 77])
         (assoc state :color-left [0 0 0]))
    :5 (if (= color-right [0 0 0])
         (assoc state :color-right [273 73 51])
         (assoc state :color-right [0 0 0]))
    :6 (if (= alpha-left 100)
         (assoc state :alpha-left 50)
         (assoc state :alpha-left 100))
    :7 (if (= alpha-right 100)
         (assoc state :alpha-right 50)
         (assoc state :alpha-right 100))
    :0 (assoc state
              :act-stroke-cap :round
              :color-left [0 0 0]
              :color-right [0 0 0]
              :alpha-left 100
              :alpha-right 100)
    state))

(q/defsketch P-2-1-1-02
  :middleware [m/fun-mode]
  :size [600 600]
  :setup setup
  :draw draw
  :mouse-pressed mouse-pressed
  :key-pressed util/key-controller
  :key-released key-released
  :settings q/smooth)
