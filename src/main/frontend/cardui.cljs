(ns frontend.cardui
  (:require
   [clojure.string :as string]
   [frontend.ui :refer [flex-center]]
   [uix.core :refer [$ defui]]
   [uix.dom]))

(defn suit-to-string [card]
  (if (nil? card)
    "nil suit"
    (case (:suit card)
      :spades "♤"
      :hearts "♡"
      :diamonds "♢"
      :clubs "♧")))

(defn rank-to-string [card]
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
      :spades "text-slate-500"
      :clubs "text-slate-500"
      :hearts "text-red-400"
      :diamonds "text-red-400")))

(defn to-string [card]
  (string/join [(suit-to-string card) (rank-to-string card)]))

(defui base [{:keys [class on-click children]}]
  ($
   :div
   {:class
    ["w-12 h-16"
     "border rounded-md border-solid border-slate-500"
     "select-none overflow-hidden"
     "bg-slate-100"
     "text-sm leading-4"
     class]
    :on-click on-click}
   children))

(defui visible [{:keys [class card on-click selected]}]
  ($ base
     {:class ["transition duration-300 shadow-md"
              (color card)
              (when selected "bg-slate-200 shadow-xl")
              class]
      :on-click on-click}
     ($ :.flex.flex-col.gap-0.5.p-1.w-6
        ($ :.text-center (suit-to-string card))
        ($ :.text-center (rank-to-string card)))))

(defui hidden []
  ($ base
     {:class ["bg-slate-200 text-xs pt-4 text-center"]}
     "Hidden"))

(defui slot []
  ($ base
     {:class ["text-slate-300 text-xs text-center" flex-center]}
     "Empty"))

(defui trump [{:keys [trump]}]
  ($ base {:class flex-center} (suit-to-string {:suit trump})))

(defui stack [{:keys [amount class]}]
  ($ base
     {:class ["text-center" flex-center class]}
     amount))

