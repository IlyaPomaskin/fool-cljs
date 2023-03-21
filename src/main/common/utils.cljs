(ns common.utils)

(defn find-item [pred coll]
  (reduce
   (fn [acc item]
     (if (not (nil? acc))
       acc
       (if (pred item)
         item
         acc)))
   nil
   coll))

(defn find-item-index [pred coll]
  (nth
   (find-item
    pred
    (map-indexed (fn [index item] [index item]) coll))
   0))
