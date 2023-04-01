(ns frontend.ui
  (:require
   [uix.core :refer [$ defui]]))

(defui panel [{:keys [class children]}]
  ($ :.rounded-md.shadow-lg.py-4.px-4.bg-slate-100 {:class class} children))

(defui title [props]
  ($ :.text-xl.pb-4.select-none props))

(def flex-center "flex justify-center items-center")