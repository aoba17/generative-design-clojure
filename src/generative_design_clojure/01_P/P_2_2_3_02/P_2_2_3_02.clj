(ns generative-design-clojure.01-P.P-2-2-3-02.P-2-2-3-02
  (:require [quil.core :as q]
            [quil.middleware :as m]
            [generative-design-clojure.util :as util]))

(def form-resolution 15)
(def step-size 2)
(def distortion-factor 1)
(def init-radius 150)

(defn setup []
  (q/stroke 0 50)
  (q/stroke-weight 0.75)
  (q/background 255)
  (let [angle (q/radians (/ 360 form-resolution))]
    {:filled   false
     :freeze   false
     :center-x (/ (q/width) 2)
     :center-y (/ (q/height) 2)
     :x        (for [i (range form-resolution)]
                 (* (q/cos (* angle i))
                    init-radius))
     :y        (for [i (range form-resolution)]
                 (* (q/sin (* angle i))
                    init-radius))
     :mode     0}))

(defn update-state [{:keys [filled] :as state}]
  (if filled
    (q/fill (q/random 255))
    (q/no-fill))
  (-> state
      (update :center-x
              (fn [center-x]
                (if (not= (q/mouse-x) 0)
                  (+ center-x
                     (* (- (q/mouse-x) center-x)
                        0.01))
                  center-x)))
      (update :center-y
              (fn [center-y]
                (if (not= (q/mouse-y) 0)
                  (+ center-y
                     (* (- (q/mouse-y) center-y)
                        0.01))
                  center-y)))
      (update :x
              (fn [x]
                (map
                  #(+ (q/random (- step-size) step-size) %)
                  x)))
      (update :y
              (fn [y]
                (map
                  #(+ (q/random (- step-size) step-size) %)
                  y)))))

(defn draw [{:keys [center-x
                    center-y
                    x
                    y
                    mode]}]
  (when (= mode 0)
    (q/begin-shape)
    (q/curve-vertex
      (+ center-x (last x))
      (+ center-y (last y)))
    (dotimes [i form-resolution]
      (q/curve-vertex
        (+ center-x (nth x i))
        (+ center-y (nth y i))))
    (q/curve-vertex
      (+ center-x (first x))
      (+ center-y (first y)))
    (q/curve-vertex
      (+ center-x (second x))
      (+ center-y (second y)))
    (q/end-shape))
  (when (= mode 1)
    (q/begin-shape)
    (q/curve-vertex
      (+ center-x (first x))
      (+ center-y (first y)))
    (dotimes [i form-resolution]
      (q/curve-vertex
        (+ center-x (nth x i))
        (+ center-y (nth y i))))
    (q/curve-vertex
      (+ center-x (last x))
      (+ center-y (last y)))
    (q/end-shape)))

(defn key-released [state {:keys [key key-code]}]
  (when (or (= key-code 8)
            (= key-code 127))
    (q/background 255))
  (case key
    :1    (assoc state :filled false)
    :2    (assoc state :filled true)
    :3    (assoc state :mode 0)
    :4    (assoc state :mode 1)
    :f    (do
            (if (:freeze state)
              (q/start-loop)
              (q/no-loop))
            (update state :freeze not))
    :up   (update state :step-size inc)
    :down (update state :step-size #(max (dec %) 1))
    state))

(defn mouse-pressed [state _]
  (case (:mode state)
    0 (let [angle  (q/radians (/ 360 form-resolution))
            radius (* init-radius (q/random 0.5 1.0))]
        (assoc state
               :center-x (q/mouse-x)
               :center-y (q/mouse-y)
               :x (for [i (range form-resolution)]
                    (* (q/cos (* angle i)) radius))
               :y (for [i (range form-resolution)]
                    (* (q/sin (* angle i)) radius))))
    1 (let [angle  0
            radius (* init-radius 4)
            x1     (* (q/cos angle) radius)
            y1     (* (q/sin angle) radius)
            x2     (* (q/cos (- angle q/PI)) radius)
            y2     (* (q/sin (- angle q/PI)) radius)]
        (assoc state
               :center-x (q/mouse-x)
               :center-y (q/mouse-y)
               :x (for [i (range form-resolution)]
                    (q/lerp x1 x2 (/ i form-resolution)))
               :y (for [i (range form-resolution)]
                    (q/lerp y1 y2 (/ i form-resolution)))))))

(q/defsketch P-2-2-3-02
  :middleware [m/fun-mode]
  :size [(q/screen-width) (q/screen-height)]
  :setup setup
  :update update-state
  :draw draw
  :key-pressed util/key-controller
  :key-released key-released
  :mouse-pressed mouse-pressed
  :settings q/smooth)
