(ns generative-design-clojure.01-P.P-1-2-2-01.P-1-2-2-01
  (:require [quil.core :as q]
            [quil.middleware :as m]
            [generative-design-clojure.util :as util]))

(def data-location "src/generative_design_clojure/01_P/P_1_2_2_01/data/")
(def pic1 (str data-location "pic1.jpg"))
(def pic2 (str data-location "pic2.jpg"))
(def pic3 (str data-location "pic3.jpg"))

(defn setup []
  (q/color-mode :hsb 360 100 100 100)
  (q/no-stroke)
  (q/no-cursor)
  {:image     (q/load-image pic1)
   :sort-mode nil})

(defn draw [{:keys [image sort-mode]}]
  (when (q/loaded? image)
    (let [tile-count (/ (q/width) (max (q/mouse-x) 5))
          rect-size  (/ (q/width) tile-count)
          colors     (atom ())]
      (loop [grid-y 0]
        (when (< grid-y tile-count)
          (loop [grid-x 0]
            (when (< grid-x tile-count)
              (let [px    (* grid-x rect-size)
                    py    (* grid-y rect-size)
                    color (q/get-pixel image px py)]
                (swap! colors conj color))
              (recur (inc grid-x))))
          (recur (inc grid-y))))

      (let [sorted-colors (util/sort-colors @colors sort-mode)
            i             (atom 0)]
        (loop [grid-y 0]
          (when (< grid-y tile-count)
            (loop [grid-x 0]
              (when (< grid-x tile-count)
                (q/fill (nth sorted-colors @i))
                (q/rect (* grid-x rect-size)
                        (* grid-y rect-size)
                        rect-size rect-size)
                (swap! i inc)
                (recur (inc grid-x))))
            (recur (inc grid-y))))))))

(defn key-control [state event]
  (case (:key event)
    :1 (assoc state :image (q/load-image pic1))
    :2 (assoc state :image (q/load-image pic2))
    :3 (assoc state :image (q/load-image pic3))
    :4 (assoc state :sort-mode nil)
    :5 (assoc state :sort-mode :hue)
    :6 (assoc state :sort-mode :saturation)
    :7 (assoc state :sort-mode :brightness)
    :8 (assoc state :sort-mode :grayscale)
    state))

(q/defsketch P_1_2_2_01
  :middleware [m/fun-mode]
  :size [600 600]
  :setup setup
  :draw draw
  :key-pressed util/key-controller
  :key-released key-control)
