(ns frontend.ui
  (:require
   [uix.core :refer [$ defui]]))

(defui panel [{:keys [class children]}]
  ($ :.rounded-md.shadow-lg.py-4.px-4.bg-slate-100 {:class class} children))

(defui title [props]
  ($ :.text-xl.pb-4.select-none props))

(def flex-center "flex justify-center items-center")

(defui button [{:keys [class size on-click children]}]
  ($ :button
     {:class [class
              (case size
                :xs "px-3 py-1 font-normal"
                :sm "px-4 py-1.5 font-medium"
                "px-6 py-2 font-medium")
              "inline-block text-xs"
              "bg-sky-400 text-white uppercase"
              "rounded shadow-md"
              "transition duration-300 ease-in-out"
              "hover:bg-sky-600 hover:shadow-lg"
              "active:bg-sky-700 active:shadow-xs"
              "focus:bg-sky-600 focus:outline-none focus:ring-0"]
      :on-click on-click}
     children))