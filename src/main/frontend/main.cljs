(ns frontend.main
  (:require
   [common.card :as c]
   [common.game :as g]
   [frontend.card-ui :as card-ui]
   [uix.core :refer [$ defui]]
   [uix.dom]))


(defui panel [{:keys [class children]}]
  ($ :.rounded-md.shadow-lg.p-2 {:class class} children))

(defui player [{:keys [player]}]
  (let [[selected set-selected!] (uix.core/use-state nil)]
    ($ panel
       {:class :.bg-slate-100}
       ($ :.text-xl.pb-4 (:id player))
       ($ :.flex.flex-row.flex-wrap.gap-2
          (map
           (fn [card]
             ($ card-ui/visible
                {:key (card-ui/to-string card)
                 :card card
                 :on-click #(set-selected! (if (c/equals? selected card) nil card))
                 :selected (c/equals? card selected)}))
           (:cards player))))))

(defui table [{:keys [table]}]
  ($ panel
     {:class :.bg-slate-100}
     ($ :.text-xl.pb-4 "Table")
     (map
      (fn [{:keys [to by]}]
        ($ :.flex.flex-col.gap-2 {:key (card-ui/to-string to)}
           ($ card-ui/visible {:card to})
           (if (nil? by)
             ($ card-ui/slot)
             ($ card-ui/visible {:card by}))))
      table)))

(defui game [{:keys [game]}]
  ($ :.flex.flex-col.gap-8
     (map
      #($ player {:key (:id %) :player %})
      (:players game))
     ($ table
        {:table [{:to {:rank :six :suit :spades} :by nil}]})))

(defui app []
  (let [[state set-value!] (uix.core/use-state (g/make-in-progress ["qwe" "ads" "zxc"]))]
    ($ :.bg-slate-50.p-8.h-full.w-full
       ($ :.container
          ($ game {:game state})))))

(defonce root
  (uix.dom/create-root js/document.body))

(defn init []
  (uix.dom/render-root ($ app) root))
