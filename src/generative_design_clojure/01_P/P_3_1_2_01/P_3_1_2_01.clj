(ns generative-design-clojure.01-P.P-3-1-2-01.P-3-1-2-01
  (:require [quil.core :as q]
            [quil.middleware :as m]
            [generative-design-clojure.util :as util]))

(def data-location "src/generative_design_clojure/01_P/P_3_1_2_01/data/")

(defn setup []
  (q/text-font (q/create-font (str data-location "miso-bold.ttf") 25))
  (q/cursor :hand)
  {:text-typed      (str "Ich bin der Musikant mit Taschenrechner in der Hand!\n\n"
                         "Ich addiere\n"
                         "Und subtrahiere, \n\n"
                         "Kontrolliere\nUnd komponiere\nUnd wenn ich diese Taste drück,\nSpielt er ein kleines Musikstück?\n\n"
                         "Ich bin der Musikant mit Taschenrechner in der Hand!\n\n"
                         "Ich addiere\n"
                         "Und subtrahiere, \n\n"
                         "Kontrolliere\nUnd komponiere\nUnd wenn ich diese Taste drück,\nSpielt er ein kleines Musikstück?\n\n")
   :center-x        (/ (q/width) 2)
   :center-y        (/ (q/height) 2)
   :offset-x        0
   :offset-y        0
   :zoom            0.75
   :act-random-seed 6
   :space           (q/load-shape (str data-location "space.svg"))
   :space2          (q/load-shape (str data-location "space2.svg"))
   :period          (q/load-shape (str data-location "period.svg"))
   :comma           (q/load-shape (str data-location "comma.svg"))
   :exclamationmark (q/load-shape (str data-location "exclamationmark.svg"))
   :questionmark    (q/load-shape (str data-location "questionmark.svg"))
   :return          (q/load-shape (str data-location "return.svg"))})

(defn draw [{:keys [text-typed center-x center-y offset-x offset-y
                    zoom act-random-seed space space2 period comma
                    exclamationmark questionmark return]}]
  (q/background 255)
  (q/no-stroke)
  (q/text-align :left)
  (q/random-seed act-random-seed)
  (q/translate center-x center-y)
  (q/scale zoom)
  (doseq [letter text-typed]
    (case letter
      \space   (if (< 1 (q/random 0 2))
                 (do (q/shape space 0 0)
                     (q/translate 1.9 0)
                     (q/rotate (/ q/PI 4)))
                 (do (q/shape space2 0 0)
                     (q/translate 13 -5)
                     (q/rotate (/ q/PI -4))))
      \,       (do (q/shape comma 0 0)
                   (q/translate 34 15)
                   (q/rotate (/ q/PI 4)))
      \.       (do (q/shape period 0 0)
                   (q/translate 56 -54)
                   (q/rotate (/ q/PI -2)))
      \!       (do (q/shape exclamationmark 0 0)
                   (q/translate 42 -17.4)
                   (q/rotate (/ q/PI -4)))
      \?       (do (q/shape questionmark 0 0)
                   (q/translate 42 -18)
                   (q/rotate (/ q/PI -4)))
      \newline (do (q/shape return 0 0)
                   (q/translate 0 10)
                   (q/rotate q/PI))
      (do (q/fill 0)
          (q/text (str letter) 0 0)
          (q/translate (q/text-width (str letter)) 0))))
  (q/fill 0)
  (when (= (mod (/ (q/frame-count) 6) 2) 0)
    (q/rect 0 0 15 2)))

(defn mouse-pressed [{:keys [center-x
                             center-y]
                      :as   state}
                     {:keys [x y]}]
  (assoc state
         :offset-x (- x center-x)
         :offset-y (- y center-y)))

(defn key-released [state event]
  (case (:key event)
    :control (do (util/save-frame)
                 state)
    :alt     (let [act-random-seed (inc (:act-random-seed state))]
               (println act-random-seed)
               (assoc state :act-random-seed act-random-seed))
    state))

(defn key-pressed [state {:keys [key key-code raw-key]}]
  (if (or (= key-code 8)
          (= key-code 127))
    (update state :text-typed (fn [text-typed]
                                (subs text-typed
                                      0
                                      (max 0 (-> text-typed count dec)))))
    (case key
      (:esc :tab)      state
      (:enter :return) (update state :text-typed str "\n")
      :up              (update state :zoom + 0.05)
      :down            (update state :zoom - 0.05)
      (update state :text-typed str raw-key))))

(q/defsketch P-3-1-2-01
  :middleware [m/fun-mode]
  :size [(q/screen-width) (q/screen-height)]
  :setup setup
  :draw draw
  :mouse-pressed mouse-pressed
  :key-released key-released
  :key-pressed key-pressed
  :features [:resizable]
  :settings q/smooth)
