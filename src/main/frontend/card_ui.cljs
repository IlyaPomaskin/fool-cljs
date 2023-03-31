(ns frontend.card-ui
  (:require
   [clojure.string :as string]
   [uix.core :refer [$ defui]]
   [uix.dom]))

(defn suit [card]
  (if (nil? card)
    "nil suit"
    (case (:suit card)
      :spades "♤"
      :hearts "♡"
      :diamonds "♢"
      :clubs "♧")))

(defn rank [card]
  (if (nil? card)
    "nil rank"
    (case (:rank card)
      :six "6"
      :seven "7"
      :eight "8"
      :nine "9"
      :ten "10"
      :jack "J"
      :queen "Q"
      :king "K"
      :ace "A")))

(defn color [card]
  (if (nil? card)
    "unknown"
    (case (:suit card)
      :spades "text-slate-600"
      :clubs "text-slate-600"
      :hearts "text-fuchsia-600"
      :diamonds "text-fuchsia-600")))

(defn to-string [card]
  (string/join [(suit card) (rank card)]))

(defui base [{:keys [class on-click children]}]
  ($
   :div
   {:class
    ["relative w-12 h-16"
     "border rounded-md border-solid border-slate-500"
     "select-none overflow-hidden"
     "text-[16px] leading-[16px]"
     class]
    :on-click on-click}
   children))

(defui visible [{:keys [card on-click selected]}]
  ($ base
     {:class [(color card) "shadow-md" (when selected "bg-slate-300")]
      :on-click on-click}
     ($ :.flex.flex-col.gap-0.5.absolute.top-1.left-1
        ($ :.text-center (suit card))
        ($ :.text-center (rank card)))))

(defui hidden []
  ($ base
     {:class ["bg-slate-200 text-xs pt-4 text-center"]}
     "Hidden"))

(defui slot []
  ($ base
     {:class ["text-slate-300 text-xs pt-4 text-center"]}
     "Empty"))