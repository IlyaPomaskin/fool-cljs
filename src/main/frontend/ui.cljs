(ns frontend.ui
  (:require
   [uix.core :refer [$ defui]]))

(defui panel [{:keys [class children]}]
  ($ :.rounded-md.shadow-lg.p-2 {:class class} children))

(defui title [props]
  ($ :.text-xl.pb-4 props))

(def flex-center "flex justify-center items-center")