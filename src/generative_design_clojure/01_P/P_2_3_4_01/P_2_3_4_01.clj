(ns generative-design-clojure.01-P.P-2-3-4-01.P-2-3-4-01
  (:require [quil.core :as q]
            [quil.middleware :as m]
            [generative-design-clojure.util :as util]))

(def data-location "src/generative_design_clojure/01_P/P_2_3_4_01/data/")

(defn setup []
  (q/background 255)
  (q/cursor :cross)

  ;; load an image in background
  ;; (let [img (q/load-image "select a background image")]
  ;;   (q/image img 0 0 (q/width) (q/height)))

  {:x           0
   :y           0
   :step-size   5
   :module-size 25
   :line-module (q/load-shape (str data-location "01.svg"))})

(defn draw [{:keys [x y step-size module-size line-module]}]
  (when (q/mouse-pressed?)
    (let [d (q/dist x y (q/mouse-x) (q/mouse-y))]
      (when (> d step-size)
        (let [angle (q/atan2 (- (q/mouse-y) y) (- (q/mouse-x) x))]
          (q/with-translation [(q/mouse-x) (q/mouse-y)]
            (q/rotate (+ angle q/PI))
            (q/shape line-module 0 0 d module-size))
          (swap! (q/state-atom) update :x #(+ %
                                              (* (q/cos angle)
                                                 step-size)))
          (swap! (q/state-atom) update :y #(+ %
                                              (* (q/sin angle)
                                                 step-size))))))))

(defn mouse-pressed [state {:keys [x y]}]
  (assoc state
         :x x
         :y y))

(defn key-released [state {:keys [key key-code]}]
  (when (or (= key-code 8)
            (= key-code 127))
    (q/background 255))
  (case key
    :1     (assoc state :line-module (q/load-shape (str data-location "01.svg")))
    :2     (assoc state :line-module (q/load-shape (str data-location "02.svg")))
    :3     (assoc state :line-module (q/load-shape (str data-location "03.svg")))
    :4     (assoc state :line-module (q/load-shape (str data-location "04.svg")))
    :5     (assoc state :line-module (q/load-shape (str data-location "05.svg")))
    :6     (assoc state :line-module (q/load-shape (str data-location "06.svg")))
    :7     (assoc state :line-module (q/load-shape (str data-location "07.svg")))
    :8     (assoc state :line-module (q/load-shape (str data-location "08.svg")))
    :9     (assoc state :line-module (q/load-shape (str data-location "09.svg")))
    :up    (update state :module-size + 5)
    :down  (update state :module-size - 5)
    :left  (update state :step-size #(max (- % 0.5) 0.5))
    :right (update state :step-size + 0.5)
    state))

(q/defsketch P-2-3-4-01
  :middleware [m/fun-mode]
  :size [(q/screen-width) (q/screen-height)]
  :setup setup
  :draw draw
  :mouse-pressed mouse-pressed
  :key-pressed util/key-controller
  :key-released key-released
  :settings q/smooth)
