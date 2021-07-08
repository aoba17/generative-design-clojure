(ns generative-design-clojure.01-P.P-4-3-2-01.P-4-3-2-01
  (:require [quil.core :as q]
            [quil.middleware :as m]
            [generative-design-clojure.util :as util]))

(def data-location "src/generative_design_clojure/01_P/P_4_3_2_01/data/")

(defn setup []
  {:input-text       (cycle "Ihr naht euch wieder, schwankende Gestalten, Die früh sich einst dem trüben Blick gezeigt. Versuch ich wohl, euch diesmal festzuhalten? Fühl ich mein Herz noch jenem Wahn geneigt? Ihr drängt euch zu! nun gut, so mögt ihr walten, Wie ihr aus Dunst und Nebel um mich steigt; Mein Busen fühlt sich jugendlich erschüttert Vom Zauberhauch, der euren Zug umwittert. Ihr bringt mit euch die Bilder froher Tage, Und manche liebe Schatten steigen auf; Gleich einer alten, halbverklungnen Sage Kommt erste Lieb und Freundschaft mit herauf; Der Schmerz wird neu, es wiederholt die Klage.")
   :font-size-max    20
   :font-size-min    10
   :spacing          12
   :kerning          0.5
   :font-size-static false
   :black-and-white  false
   :font             (q/create-font "Times" 10)
   :img              (q/load-image (str data-location "pic.png"))})

(defn draw [{:keys [input-text
                    font-size-max
                    font-size-min
                    spacing
                    kerning
                    font-size-static
                    black-and-white
                    font
                    img]}]
  (q/background 255)
  (q/text-align :left)
  (loop [x       0
         y       10
         counter 0]
    (when (< y (q/height))
      (let [img-x     (q/map-range x 0 (q/width) 0 (.-width img))
            img-y     (q/map-range y 0 (q/height) 0 (.-height img))
            c         (q/get-pixel img img-x img-y)
            greyscale (q/round (+ (* (q/red c) 0.222)
                                  (* (q/green c) 0.707)
                                  (* (q/blue c) 0.071)))
            char      (str (nth input-text counter))]
        (q/with-translation [x y]
          (if font-size-static
            (do
              (q/text-font font font-size-max)
              (if black-and-white
                (q/fill greyscale)
                (q/fill c)))
            (let [font-size (max (q/map-range greyscale
                                              0 255
                                              font-size-max font-size-min)
                                 1)]
              (q/text-font font font-size)
              (if black-and-white
                (q/fill 0)
                (q/fill c))))
          (q/text char 0 0))
        (let [letter-width (+ (q/text-width char)
                              kerning)
              text-end     (+ x letter-width)]
          (recur (if (>= text-end (q/width))
                   0
                   text-end)
                 (if (>= text-end (q/width))
                   (+ y spacing)
                   y)
                 (inc counter)))))))

(defn key-released [state event]
  (case (:key event)
    :s (do (util/save-frame)
           state)
    :1 (update state :font-size-static not)
    :2 (update state :black-and-white not)
    state))

(defn key-pressed [state event]
  (case (:key event)
    :up    (update state :font-size-max + 2)
    :down  (update state :font-size-max - 2)
    :right (update state :font-size-min + 2)
    :left  (update state :font-size-min - 2)
    state))

(q/defsketch P-4-3-2-01
  :middleware [m/fun-mode]
  :size [533 769]
  :setup setup
  :draw draw
  :key-released key-released
  :key-pressed key-pressed
  :features [:resizable]
  :settings q/smooth)
