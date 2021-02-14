(ns generative-design-clojure.01-P.P-2-3-1-02.P-2-3-1-02
  (:require [quil.core :as q]
            [quil.middleware :as m]
            [generative-design-clojure.util :as util]))

(def data-location "src/generative_design_clojure/01_P/P_2_3_1_02/data/")

(defn setup []
  (q/background 255)
  (q/cursor :cross)
  {:color            (q/color 181 157 0 100)
   :angle            0
   :angle-speed      1
   :line-module-size 0
   :line-module      nil
   :click-posx       0
   :click-posy       0})

(defn update-state [{:keys [angle-speed]
                     :as   state}]
  (if (q/mouse-pressed?)
    (update state :angle + angle-speed)
    state))

(defn draw [{:keys [color
                    angle
                    line-module-size
                    line-module
                    click-posx
                    click-posy]}]
  (when (q/mouse-pressed?)
    (q/stroke-weight 0.75)
    (q/no-fill)
    (q/stroke (q/red color)
              (q/green color)
              (q/blue color)
              (q/alpha color))
    (let [x (atom (q/mouse-x))
          y (atom (q/mouse-y))]
      (when (and (q/key-pressed?)
                 (= (q/key-as-keyword) :shift))
        (if (> (q/abs (- click-posx @x))
               (q/abs (- click-posy @y)))
          (reset! y click-posy)
          (reset! x click-posx)))
      (q/with-translation [@x @y]
        (q/rotate (q/radians angle))
        (if (not= line-module nil)
          (do
            (.disableStyle line-module)
            (q/shape line-module 0 0 line-module-size line-module-size))
          (q/line 0 0 line-module-size line-module-size))))))

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
    :5     (assoc state :line-module nil)
    :6     (assoc state :line-module (q/load-shape (str data-location "02.svg")))
    :7     (assoc state :line-module (q/load-shape (str data-location "03.svg")))
    :8     (assoc state :line-module (q/load-shape (str data-location "04.svg")))
    :9     (assoc state :line-module (q/load-shape (str data-location "05.svg")))
    :up    (update state :line-length + 5)
    :down  (update state :line-length - 5)
    :left  (update state :angle-speed - 0.5)
    :right (update state :angle-speed + 0.5)
    state))

(defn mouse-pressed [state _]
  (assoc state
         :line-module-size (q/random 50 160)
         :click-posx (q/mouse-x)
         :click-posy (q/mouse-y)))

(q/defsketch P-2-3-1-02
  :middleware [m/fun-mode]
  :size [(q/screen-width) (q/screen-height)]
  :setup setup
  :update update-state
  :draw draw
  :mouse-pressed mouse-pressed
  :key-pressed util/key-controller
  :key-released key-released
  :settings q/smooth)
