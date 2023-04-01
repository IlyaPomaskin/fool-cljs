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
       {:class :.bg-slate-100}
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

(defui table [{:keys [table]}]
  ($ ui/panel
     {:class :.bg-slate-100}
     ($ ui/title "Table")
     (map
      (fn [{:keys [to by]}]
        ($ :.flex.flex-col.gap-2 {:key (card-ui/to-string to)}
           ($ card-ui/visible {:card to})
           (if (nil? by)
             ($ card-ui/slot)
             ($ card-ui/visible {:card by}))))
      table)))

(defui stack [{:keys [deck]}]
  (let []
    ($ :div)))

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
  (uix.dom/create-root (js/document.getElementById "root")))

(defn init []
  (uix.dom/render-root ($ app) root))