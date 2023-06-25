(ns frontend.stackui
  (:require
   [frontend.cardui :as card-ui]
   [uix.core :refer [$ defui]]
   [uix.dom]))

(defui stack [{:keys [deck trump]}]
  (let [length (dec (count deck))
        has-cards? (> length 0)
        last-card (last deck)]
    ($ :.relative.w-24
       (if (or has-cards? last-card)
         ($ card-ui/slot {:class "relative z-10"})
         ($ card-ui/trump {:trump trump}))

       (when has-cards?
         ($ card-ui/stack
            {:class "absolute top-0 z-10"
             :amount length}))

       (when last-card
         ($ card-ui/visible
            {:class "absolute top-0 rotate-90 left-8"
             :card last-card})))))