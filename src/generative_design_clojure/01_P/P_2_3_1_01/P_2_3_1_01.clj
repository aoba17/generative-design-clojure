(ns generative-design-clojure.01-P.P-2-3-1-01.P-2-3-1-01
  (:require [quil.core :as q]
            [quil.middleware :as m]
            [generative-design-clojure.util :as util]))

(def is-mouse-pressed (atom false))

(defn setup []
  (q/background 255)
  (q/cursor :cross)
  {:color       (q/color 181 157 0 100)
   :line-length 0
   :angle       0
   :angle-speed 1})

(defn update-state [{:keys [angle-speed]
                     :as   state}]
  (if @is-mouse-pressed
    (update state :angle + angle-speed)
    state))

(defn draw [{:keys [color line-length angle]}]
  (when @is-mouse-pressed
    (q/stroke-weight 1)
    (q/no-fill)
    (q/stroke (q/red color)
              (q/green color)
              (q/blue color)
              (q/alpha color))
    (q/with-translation [(q/mouse-x) (q/mouse-y)]
      (q/rotate (q/radians angle))
      (q/line 0 0 line-length 0))))

(defn key-released [state {:keys [key key-code]}]
  (when (or (= key-code 8)
            (= key-code 127))
    (q/background 255))
  (case key
    :d     (-> state
               (update :angle + 180)
               (update :angle-speed * -1))
    :space (assoc state :color (q/color (q/random 255)
                                        (q/random 255)
                                        (q/random 255)
                                        (q/random 255)))
    :1     (assoc state :color (q/color 181 157 0 100))
    :2     (assoc state :color (q/color 0 130 164 100))
    :3     (assoc state :color (q/color 87 35 129 100))
    :4     (assoc state :color (q/color 197 0 123 100))
    :up    (update state :line-length + 5)
    :down  (update state :line-length - 5)
    :left  (update state :angle-speed - 0.5)
    :right (update state :angle-speed + 0.5)
    state))

(defn mouse-pressed [state _]
  (reset! is-mouse-pressed true)
  (assoc state :line-length (q/random 70 200)))

(defn mouse-released [state _]
  (reset! is-mouse-pressed false)
  state)

(q/defsketch P-2-3-1-01
  :middleware [m/fun-mode]
  :size [(q/screen-width) (q/screen-height)]
  :setup setup
  :update update-state
  :draw draw
  :mouse-pressed mouse-pressed
  :mouse-released mouse-released
  :key-pressed util/key-controller
  :key-released key-released
  :settings q/smooth)
