(ns generative-design-clojure.01-P.P-2-3-3-01.P-2-3-3-01
  (:require [quil.core :as q]
            [quil.middleware :as m]
            [generative-design-clojure.util :as util]))

(def letters "Sie hören nicht die folgenden Gesänge, Die Seelen, denen ich die ersten sang, Zerstoben ist das freundliche Gedränge, Verklungen ach! der erste Wiederklang.")
(def font-size-min 3)

(defn setup []
  (q/background 255)
  (q/cursor :cross)
  (q/fill 0)
  {:x                (q/mouse-x)
   :y                (q/mouse-y)
   :angle-distortion 0
   :counter          0
   :font             (q/create-font "AmericanTypewriter" 10)})

(defn draw [{:keys [x y angle-distortion counter font]}]
  (q/text-align :left)
  (when (q/mouse-pressed?)
    (let [d          (q/dist x y (q/mouse-x) (q/mouse-y))
          new-letter (-> letters (nth counter) str)
          step-size  (q/text-width new-letter)]
      (q/text-font font (/ (+ font-size-min d) 2))
      (when (> d step-size)
        (let [angle (q/atan2 (- (q/mouse-y) y)
                             (- (q/mouse-x) x))]
          (q/with-translation [x y]
            (q/rotate (+ angle (q/random angle-distortion)))
            (q/text new-letter 0 0))
          (if (= counter (-> letters count dec))
            (swap! (q/state-atom) assoc :counter 0)
            (swap! (q/state-atom) update :counter inc))
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
    :up   (update state :angle-distortion + 0.1)
    :down (update state :angle-distortion - 0.1)))

(q/defsketch P-2-3-3-01
  :middleware [m/fun-mode]
  :size [(q/screen-width) (q/screen-height)]
  :setup setup
  :draw draw
  :mouse-pressed mouse-pressed
  :key-pressed util/key-controller
  :key-released key-released
  :settings q/smooth)
