(ns generative-design-clojure.01-P.P-2-2-2-02.P-2-2-2-02
  (:require [quil.core :as q]
            [quil.middleware :as m]
            [generative-design-clojure.util :as util]))

(def angle-count 7)
(def step-size 3)
(def min-length 10)

(defn get-random-angle [direction]
  (let [a (*  (+ (q/floor (q/random (- angle-count) angle-count)) 0.5)
              (/ 90 angle-count))]
    (case direction
      :north (- a 90)
      :east  a
      :south (+ a 90)
      :west  (+ a 180)
      0)))

(defn setup []
  (q/color-mode :hsb 360 100 100 100)
  (q/background 360)
  (let [pos-x     (q/floor (q/random (q/width)))
        pos-y     5
        direction :south]
    {:pos-x       (atom pos-x)
     :pos-y       (atom pos-y)
     :pos-x-cross (atom pos-x)
     :pos-y-cross (atom pos-y)
     :direction   (atom direction)
     :angle       (atom (get-random-angle direction))
     :draw-mode   1}))

(defn draw [{:keys [pos-x
                    pos-y
                    pos-x-cross
                    pos-y-cross
                    direction
                    angle
                    draw-mode]}]
  (doseq [_ (range (q/mouse-x))]
    (swap! pos-x + (* (q/cos (q/radians @angle)) step-size))
    (swap! pos-y + (* (q/sin (q/radians @angle)) step-size))
    (let [reached-border (atom false)]
      (cond
        (<= @pos-y 5)                (do
                                       (reset! direction :south)
                                       (reset! reached-border true))
        (>= @pos-x (- (q/width) 5))  (do
                                       (reset! direction :west)
                                       (reset! reached-border true))
        (>= @pos-y (- (q/height) 5)) (do
                                       (reset! direction :north)
                                       (reset! reached-border true))
        (<= @pos-x 5)                (do
                                       (reset! direction :east)
                                       (reset! reached-border true)))
      (when (or @reached-border
                (not= (q/get-pixel @pos-x @pos-y)
                      (q/color 360)))
        (reset! angle (get-random-angle @direction))
        (let [distance (q/dist @pos-x @pos-y @pos-x-cross @pos-y-cross)
              d-weight 50
              d-stroke 4]
          (when (>= distance  min-length)
            (q/stroke-weight (/ distance d-weight))
            (case draw-mode
              1 (q/stroke 0)
              2 (q/stroke 52 100 (/ distance d-stroke))
              3 (q/stroke 192 100 64 (/ distance d-stroke)))
            (q/line @pos-x @pos-y @pos-x-cross @pos-y-cross)))
        (reset! pos-x-cross @pos-x)
        (reset! pos-y-cross @pos-y)))))

(defn key-released [state {:keys [key key-code]}]
  (when (or (= key-code 8)
            (= key-code 127))
    (q/background 360))
  (case key
    :1 (assoc state :draw-mode 1)
    :2 (assoc state :draw-mode 2)
    :3 (assoc state :draw-mode 3)
    state))

(q/defsketch P-2-2-2-02
  :middleware [m/fun-mode]
  :size [600 600]
  :setup setup
  :draw draw
  :key-pressed util/key-controller
  :key-released key-released
  :settings q/smooth)
