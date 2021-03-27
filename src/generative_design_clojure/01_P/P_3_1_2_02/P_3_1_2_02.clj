(ns generative-design-clojure.01-P.P-3-1-2-02.P-3-1-2-02
  (:require [quil.core :as q]
            [quil.middleware :as m]
            [generative-design-clojure.util :as util]
            [clojure.string :as string]))

(def data-location "src/generative_design_clojure/01_P/P_3_1_2_02/data/")

(defn setup []
  (q/text-font (q/create-font (str data-location "miso-bold.ttf") 25))
  (q/cursor :hand)
  (let [space           (q/load-shape (str data-location "space.svg"))
        space2          (q/load-shape (str data-location "space2.svg"))
        period          (q/load-shape (str data-location "period.svg"))
        comma           (q/load-shape (str data-location "comma.svg"))
        exclamationmark (q/load-shape (str data-location "exclamationmark.svg"))
        questionmark    (q/load-shape (str data-location "questionmark.svg"))
        icon1           (q/load-shape (str data-location "icon1.svg"))
        icon2           (q/load-shape (str data-location "icon2.svg"))
        icon3           (q/load-shape (str data-location "icon3.svg"))
        icon4           (q/load-shape (str data-location "icon4.svg"))
        icon5           (q/load-shape (str data-location "icon5.svg"))]
    (.disableStyle space)
    (.disableStyle space2)
    (.disableStyle period)
    (.disableStyle comma)
    (.disableStyle exclamationmark)
    (.disableStyle questionmark)
    {:text-typed      "Was hier folgt ist Text! So asnt, und mag. Ich mag Text sehr."
     :center-x        (/ (q/width) 2)
     :center-y        (/ (q/height) 2)
     :zoom            0.75
     :space           space
     :space2          space2
     :period          period
     :comma           comma
     :exclamationmark exclamationmark
     :questionmark    questionmark
     :icon1           icon1
     :icon2           icon2
     :icon3           icon3
     :icon4           icon4
     :icon5           icon5
     :palette         (cycle [(q/color 253 195 0)
                              (q/color 0)
                              (q/color 0 158 224)
                              (q/color 99 33 129)
                              (q/color 121 156 19)
                              (q/color 226 0 26)
                              (q/color 224 134 178)])}))

(defn draw [{:keys [text-typed center-x center-y zoom space space2
                    period comma exclamationmark questionmark
                    icon1 icon2 icon3 icon4 icon5 palette]}]
  (q/background 255)
  (q/no-stroke)
  (q/text-align :left)
  (q/scale zoom)
  (q/random-seed 0)
  (let [act-color-index (atom 0)]
    (q/with-translation [center-x center-y]
      (q/fill (nth palette @act-color-index))
      (q/rect 0 -25 10 35)
      (dotimes [i (count text-typed)]
        (let [letter       (nth text-typed i)
              letter-str   (str letter)
              letter-width (q/text-width letter-str)]
          (case letter
            \space   (case (-> 5 q/random q/floor)
                       0 (do (q/shape space 0 0)
                             (q/translate 1.9 0)
                             (q/rotate (/ q/PI 4)))
                       1 (do (q/shape space2 0 0)
                             (q/translate 13 -5)
                             (q/rotate (/ q/PI -4)))
                       nil)
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
            \newline (do (q/rect 0 -25 10 35)
                         (q/translate (q/random -300 300) (q/random -300 300))
                         (q/rotate (-> 8
                                       q/random
                                       q/floor
                                       (* (/ q/PI 4))))
                         (swap! act-color-index inc)
                         (q/fill (nth palette @act-color-index))
                         (q/rect 0 -25 10 35))
            \o       (do (q/rect 0 -15 (inc letter-width)15)
                         (q/push-style)
                         (q/fill 0)
                         (-> text-typed
                             (subs (max 0 (- i 10)) i)
                             (string/replace #" " "")
                             (string/capitalize)
                             (q/text -10 40))
                         (q/ellipse -5 -7 33 33)
                         (q/fill 255)
                         (q/ellipse -5 -7 25 25)
                         (q/pop-style)
                         (q/translate letter-width 0))
            \a       (do (q/rect 0 -15 (inc letter-width) 25)
                         (q/translate letter-width 0))
            \u       (do (q/rect 0 -25 (inc letter-width) 25)
                         (q/translate letter-width 0))
            \:       (q/shape icon1 0 -60 30 30)
            \+       (q/shape icon2 0 -60 35 30)
            \-       (q/shape icon3 0 -60 30 30)
            \x       (q/shape icon4 0 -60 30 30)
            \z       (q/shape icon5 0 -60 30 30)
            (do (q/rect 0 -15 (inc letter-width) 15)
                (q/translate letter-width 0)))))
      (q/fill 200 30 40)
      (when (= (mod (/ (q/frame-count) 6) 2) 0)
        (q/rect 0 0 15 2)))))

(defn mouse-pressed [state {:keys [x y]}]
  (assoc state
         :center-x x
         :center-y y))

(defn key-released [state event]
  (case (:key event)
    :control (do (util/save-frame)
                 state)
    state))

(defn key-pressed [state {:keys [key key-code raw-key]}]
  (if (or (= key-code 8)
          (= key-code 127))
    (update state :text-typed (fn [text-typed]
                                (subs text-typed
                                      0
                                      (max 0 (-> text-typed count dec)))))
    (case key
      (:esc :tab :control) state
      (:enter :return)     (update state :text-typed str "\n")
      :up                  (update state :zoom + 0.05)
      :down                (update state :zoom - 0.05)
      (update state :text-typed str raw-key))))

(q/defsketch P-3-1-2-02
  :middleware [m/fun-mode]
  :size [800 600]
  :setup setup
  :draw draw
  :mouse-pressed mouse-pressed
  :key-released key-released
  :key-pressed key-pressed
  :features [:resizable]
  :settings q/smooth)
