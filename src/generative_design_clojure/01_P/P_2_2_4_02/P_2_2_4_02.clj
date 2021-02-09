(ns generative-design-clojure.01-P.P-2-2-4-02.P-2-2-4-02
  (:require [quil.core :as q]
            [quil.middleware :as m]
            [generative-design-clojure.util :as util]))

(def max-count 5000)

(defn setup []
  (q/background 255)
  {:x             [(/ (q/width) 2)]
   :y             [(/ (q/height) 2)]
   :r             [360]
   :current-count 1
   :new-xv        [nil]
   :new-yv        [nil]
   :draw-ghosts   false})

(defn update-state [{:keys [x y r current-count]
                     :as   state}]
  (q/background 255)
  (let [new-r         (q/random 1 7)
        new-x         (q/random new-r (- (q/width) new-r))
        new-y         (q/random new-r (- (q/height) new-r))
        closest-dist  (atom 10000000)
        closest-index (atom 0)]
    (dotimes [i current-count]
      (let [new-dist (q/dist new-x
                             new-y
                             (nth x i)
                             (nth y i))]
        (when (< new-dist @closest-dist)
          (reset! closest-dist new-dist)
          (reset! closest-index i))))
    (let [angle (q/atan2 (- new-y (nth y @closest-index))
                         (- new-x (nth x @closest-index)))]
      (-> state
          (update :new-xv conj new-x)
          (update :new-yv conj new-y)
          (update :x conj (+ (nth x @closest-index)
                             (* (q/cos angle)
                                (+ (nth r @closest-index)
                                   new-r))))
          (update :y conj (+ (nth y @closest-index)
                             (* (q/sin angle)
                                (+ (nth r @closest-index)
                                   new-r))))
          (update :r conj new-r)
          (update :current-count inc)))))

(defn draw [{:keys [x y r current-count draw-ghosts new-xv new-yv]}]
  (q/stroke-weight 0.5)

  (when draw-ghosts
    (doseq [i (-> current-count range rest)]
      (q/fill 230)
      (q/ellipse (nth new-xv i)
                 (nth new-yv i)
                 (* (nth r i) 2)
                 (* (nth r i) 2))
      (q/line (nth new-xv i)
              (nth new-yv i)
              (nth x i)
              (nth y i))))

  (dotimes [i current-count]
    (if (= i 0)
      (q/no-fill)
      (q/fill 50))
    (q/ellipse (nth x i)
               (nth y i)
               (* (nth r i) 2)
               (* (nth r i) 2)))

  (when (>= current-count max-count)
    (q/no-loop)))

(defn key-released [state event]
  (case (:key event)
    :1 (update state :draw-ghosts not)
    state))

(q/defsketch P-2-2-4-02
  :middleware [m/fun-mode]
  :size [800 800]
  :setup setup
  :update update-state
  :draw draw
  :key-pressed util/key-controller
  :key-released key-released
  :settings q/smooth)
