(ns generative-design-clojure.01-P.P-3-1-1-01.P-3-1-1-01
  (:require [quil.core :as q]
            [quil.middleware :as m]
            [generative-design-clojure.util :as util]))

(def min-font-size 15)
(def max-font-size 800)
(def max-time-delta 5000)
(def tracking 0)

(defn setup []
  (q/no-cursor)
  (let [text-typed "Type slow and fast!"]
    {:text-typed    text-typed
     :font-sizes    (for [_ (-> text-typed count range)]
                      min-font-size)
     :new-font-size 0
     :p-millis      (q/millis)
     :font          (q/create-font "ArialMT" 10)}))

(defn draw [{:keys [text-typed
                    font-sizes
                    p-millis
                    font]}]
  (q/background 255)
  (q/text-align :left)
  (q/fill 0)
  (q/no-stroke)
  (let [spacing (q/map-range (q/mouse-y) 0 (q/height) 0 120)
        x       (atom 0)
        y       (atom 0)]
    (q/translate 0 (+ 200 spacing))
    (dotimes [i (count text-typed)]
      (q/text-font font (nth font-sizes i))
      (let [letter       (str (nth text-typed i))
            letter-width (+ (q/text-width letter) tracking)]
        (when (> (+ @x letter-width) (q/width))
          (reset! x 0)
          (swap! y + spacing))
        (q/text letter @x @y)
        (swap! x + letter-width)))
    (let [time-delta    (- (q/millis) p-millis)
          new-font-size (min (q/map-range
                               time-delta
                               0
                               max-time-delta
                               min-font-size
                               max-font-size)
                             max-font-size)]
      (swap! (q/state-atom) assoc :new-font-size new-font-size)
      (if (zero? (mod (/ (q/frame-count) 10) 2))
        (q/fill 255)
        (q/fill 200 30 40))
      (q/rect @x @y (/ new-font-size 2) (/ new-font-size 20)))))

(defn key-released [state event]
  (when (= (:key event) :control)
    (util/save-frame))
  state)

(defn key-pressed [{:keys [text-typed
                           new-font-size]
                    :as   state}
                   {:keys [key key-code raw-key]}]
  (let [reset-timer (fn [s] (assoc s :p-millis (q/millis)))]
    (if (or (= key-code 8)
            (= key-code 127))
      (when (> (count text-typed) 0)
        (-> state
            (update :text-typed subs 0 (max 0 (dec (count text-typed))))
            (update :font-sizes drop-last)
            reset-timer))
      (case key
        (:tab :enter :return :esc) (reset-timer state)
        (-> state
            (update :text-typed str raw-key)
            (update :font-sizes #(-> %
                                     reverse
                                     (conj new-font-size)
                                     reverse))
            reset-timer)))))

(q/defsketch P-3-1-1-01
  :middleware [m/fun-mode]
  :size [800 600]
  :setup setup
  :draw draw
  :key-released key-released
  :key-pressed key-pressed
  :features [:resizable]
  :settings q/smooth)
