(ns generative-design-clojure.01-P.P-2-1-2-02.P-2-1-2-02
  (:require [quil.core :as q]
            [quil.middleware :as m]
            [generative-design-clojure.util :as util]))

(defn setup []
  (q/color-mode :hsb 360 100 100 100)
  {:module-color-background  (q/color 0)
   :module-color-foreground  (q/color 360)
   :module-alpha-background  100
   :module-alpha-foreground  100
   :module-radius-background 30
   :module-radius-foreground 15
   :back-color               (q/color 360)
   :tile-count               20
   :act-random-seed          0})

(defn draw [{:keys [module-color-background
                    module-color-foreground
                    module-alpha-background
                    module-alpha-foreground
                    module-radius-background
                    module-radius-foreground
                    back-color
                    tile-count
                    act-random-seed]}]
  (q/translate (/ (q/width) tile-count 2)
               (/ (q/height) tile-count 2))
  (q/background (q/hue back-color)
                (q/saturation back-color)
                (q/brightness back-color))
  (q/no-stroke)
  (q/random-seed act-random-seed)

  (doseq [grid-y (range tile-count)]
    (doseq [grid-x (range tile-count)]
      (let [pos-x   (* (/ (q/width) tile-count) grid-x)
            pos-y   (* (/ (q/height) tile-count) grid-y)
            shift-x (* (q/random -1 1) (/ (q/mouse-x) 20))
            shift-y (* (q/random -1 1) (/ (q/mouse-y) 20))]
        (q/fill (q/hue module-color-background)
                (q/saturation module-color-background)
                (q/brightness module-color-background)
                module-alpha-background)
        (q/ellipse (+ pos-x shift-x)
                   (+ pos-y shift-y)
                   module-radius-background
                   module-radius-background))))

  (doseq [grid-y (range tile-count)]
    (doseq [grid-x (range tile-count)]
      (let [pos-x (* (/ (q/width) tile-count) grid-x)
            pos-y (* (/ (q/height) tile-count) grid-y)]
        (q/fill (q/hue module-color-foreground)
                (q/saturation module-color-foreground)
                (q/brightness module-color-foreground)
                module-alpha-foreground)
        (q/ellipse pos-x
                   pos-y
                   module-radius-foreground
                   module-radius-foreground)))))

(defn mouse-pressed [state _]
  (assoc state :act-random-seed (q/random 100000)))

(defn key-released [{:keys [module-alpha-background]
                     :as   state} event]
  (case (:key event)
    :1     (update state :module-color-background
                   #(if (= % (q/color 0))
                      (q/color 273 73 51)
                      (q/color 0)))
    :2     (update state :module-color-foreground
                   #(if (= % (q/color 360))
                      (q/color 323 100 77)
                      (q/color 360)))
    :3     (if (= module-alpha-background 100)
             (assoc state
                    :module-alpha-background 50
                    :module-alpha-foreground 50)
             (assoc state
                    :module-alpha-background 100
                    :module-alpha-foreground 100))
    :0     (assoc state
                  :module-color-background (q/color 0)
                  :module-color-foreground (q/color 360)
                  :module-alpha-background 100
                  :module-alpha-foreground 100
                  :module-radius-background 20
                  :module-radius-foreground 10)
    :up    (update state :module-radius-background + 2)
    :down  (update state :module-radius-background #(max (- % 2) 10))
    :left  (update state :module-radius-foreground #(max (- % 2) 5))
    :right (update state :module-radius-foreground + 2)
    state))

(q/defsketch P-2-1-2-02
  :middleware [m/fun-mode]
  :size [600 600]
  :setup setup
  :draw draw
  :key-pressed util/key-controller
  :key-released key-released
  :mouse-pressed mouse-pressed
  :settings q/smooth)
