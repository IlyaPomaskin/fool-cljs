(ns frontend.main
  (:require
   [common.card :as c]
   [common.game :as g]
   [frontend.cardui :as card-ui]
   [frontend.ui :as ui]
   [uix.core :refer [$ defui]]
   [uix.dom]))

(defui player [{:keys [player]}]
  (let [[selected set-selected!] (uix.core/use-state nil)]
    ($ ui/panel
       ($ ui/title (:id player))
       ($ :.flex.flex-row.flex-wrap.gap-2
          (map
           (fn [card]
             ($ card-ui/visible
                {:key (card-ui/to-string card)
                 :card card
                 :on-click #(set-selected! (if (c/equals? selected card) nil card))
                 :selected (c/equals? card selected)}))
           (:cards player))))))

(defui table [{:keys [class table]}]
  ($ ui/panel
     {:class [class]}
     ($ ui/title "Table")

     ($ :.flex.gap-2
        (when (empty? table)
          ($ :.flex.flex-col.gap-2
             ($ card-ui/slot)))

        (map
         (fn [{:keys [to by]}]
           ($ :.flex.flex-col.gap-2 {:key (card-ui/to-string to)}
              ($ card-ui/visible {:card to})
              (if (nil? by)
                ($ card-ui/slot)
                ($ card-ui/visible {:card by}))))
         table))))

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

(defui game [{:keys [game]}]
  ($ :.flex.flex-col.gap-8
     (map
      (fn [item] ($ player {:key (:id item) :player item}))
      (:players game))

     ($ :.flex.gap-8
        ($ ui/panel
           ($ ui/title "Deck")
           ($ stack game))

        ($ table
           {:class "flex-1"
            :table (:table game)}))))

(defui app []
  (let [[state set-value!] (uix.core/use-state (g/make-in-progress ["qwe" "ads" "zxc"]))]
    ($ :.bg-slate-50.p-8.h-full.w-full
       ($ :.container
          ($ game {:game state})))))

(defonce root
  (uix.dom/create-root (js/document.getElementById "root")))

(defn init []
  (uix.dom/render-root ($ app) root))
