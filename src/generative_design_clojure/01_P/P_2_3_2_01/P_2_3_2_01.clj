(ns generative-design-clojure.01-P.P-2-3-2-01.P-2-3-2-01
  (:require [quil.core :as q]
            [quil.middleware :as m]
            [generative-design-clojure.util :as util]))

(defn setup []
  (q/background 255)
  (q/cursor :cross)
  {:draw-mode   1
   :color       (q/color (q/random 255)
                         (q/random 255)
                         (q/random 255)
                         (q/random 100))
   :x           (q/mouse-x)
   :y           (q/mouse-y)
   :step-size   5
   :line-length 25})

(defn draw [{:keys [draw-mode
                    color
                    x
                    y
                    step-size
                    line-length]}]
  (when (q/mouse-pressed?)
    (let [d     (q/dist x y (q/mouse-x) (q/mouse-y))
          angle (q/atan2 (- (q/mouse-y) y)
                         (- (q/mouse-x) x))]
      (when (> d step-size)
        (q/with-translation [x y]
          (q/rotate angle)
          (if (= (mod (q/frame-count) 2) 0)
            (q/stroke 150)
            (q/stroke (q/red color)
                      (q/green color)
                      (q/blue color)
                      (q/alpha color)))
          (q/line 0 0 0 (* line-length
                           (q/random 0.95 1)
                           (/ d 10))))
        (case draw-mode
          1 (-> (q/state-atom)
                (swap! assoc
                       :x (+ x
                             (* (q/cos angle)
                                step-size))
                       :y (+ y
                             (* (q/sin angle)
                                step-size))))
          2 (-> (q/state-atom)
                (swap! assoc
                       :x (q/mouse-x)
                       :y (q/mouse-y))))))))

(defn mouse-pressed [state {:keys [x y]}]
  (assoc state
         :x x
         :y y
         :color (q/color (q/random 255)
                         (q/random 255)
                         (q/random 255)
                         (q/random 100))))

(defn key-released [state {:keys [key key-code]}]
  (when (or (= key-code 8)
            (= key-code 127))
    (q/background 255))
  (case key
    :1    (assoc state :draw-mode 1)
    :2    (assoc state :draw-mode 2)
    :up   (update state :line-length + 5)
    :down (update state :line-length - 5)
    state))

(q/defsketch P-2-3-2-01
  :middleware [m/fun-mode]
  :size [(q/screen-width) (q/screen-height)]
  :setup setup
  :draw draw
  :mouse-pressed mouse-pressed
  :key-pressed util/key-controller
  :key-released key-released
  :settings q/smooth)
