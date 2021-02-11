(ns generative-design-clojure.01-P.P-2-2-5-01.P-2-2-5-01
  (:require [quil.core :as q]
            [quil.middleware :as m]
            [generative-design-clojure.util :as util]))

(def max-count 5000)
(def min-radius 3)
(def max-radius 50)
(def is-mouse-pressed (atom false))

(defn setup []
  (q/no-fill)
  (q/cursor :cross)
  {:current-count 1
   :x             [200]
   :y             [100]
   :r             [50]
   :closest-index [0]
   :mouse-rect    30})

(defn update-state [{:keys [current-count mouse-rect x y r]
                     :as   state}]
  (let [new-x        (if @is-mouse-pressed
                       (q/random (- (q/mouse-x) (/ mouse-rect 2))
                                 (+ (q/mouse-x) (/ mouse-rect 2)))
                       (q/random max-radius (- (q/width) max-radius)))
        new-y        (if @is-mouse-pressed
                       (q/random (- (q/mouse-y) (/ mouse-rect 2))
                                 (+ (q/mouse-y) (/ mouse-rect 2)))
                       (q/random max-radius (- (q/height) max-radius)))
        new-r        (if @is-mouse-pressed
                       1
                       min-radius)
        intersection (atom false)]
    (dotimes [i current-count]
      (when (< (q/dist new-x new-y (nth x i) (nth y i))
               (+ new-r (nth r i)))
        (reset! intersection true)))
    (if-not @intersection
      (let [new-radius (atom (q/width))
            closest    (atom 0)]
        (dotimes [i current-count]
          (let [d (q/dist new-x new-y (nth x i) (nth y i))]
            (when (> @new-radius (- d (nth r i)))
              (reset! new-radius (- d (nth r i)))
              (reset! closest i))))
        (when (> @new-radius max-radius)
          (reset! new-radius max-radius))
        (-> state
            (update :closest-index conj @closest)
            (update :x conj new-x)
            (update :y conj new-y)
            (update :r conj @new-radius)
            (update :current-count inc)))
      state)))

(defn draw [{:keys [current-count mouse-rect x y r closest-index]}]
  (q/background 255)
  (dotimes [i current-count]
    (q/stroke 0)
    (q/stroke-weight 1.5)
    (q/ellipse (nth x i)
               (nth y i)
               (* (nth r i) 2)
               (* (nth r i) 2))
    (q/stroke 226 185 0)
    (q/stroke-weight 0.75)
    (q/line (nth x i)
            (nth y i)
            (nth x (nth closest-index i))
            (nth y (nth closest-index i))))

  (when @is-mouse-pressed
    (q/stroke 255 200 0)
    (q/stroke-weight 2)
    (q/rect (- (q/mouse-x) (/ mouse-rect 2))
            (- (q/mouse-y) (/ mouse-rect 2))
            mouse-rect mouse-rect))
  (when (>= current-count max-count)
    (q/no-loop)))

(defn key-released [state event]
  (case (:key event)
    :up   (update state :mouse-rect + 4)
    :down (update state :mouse-rect - 4)
    state))

(defn mouse-pressed [state _]
  (reset! is-mouse-pressed true)
  state)

(defn mouse-released [state _]
  (reset! is-mouse-pressed false)
  state)

(q/defsketch P-2-2-5-01
  :middleware [m/fun-mode]
  :size [800 800]
  :setup setup
  :update update-state
  :draw draw
  :mouse-pressed mouse-pressed
  :mouse-released mouse-released
  :key-pressed util/key-controller
  :key-released key-released
  :settings q/smooth)
