(ns data.nmea-0183.input
  "IO facilities for reading from input sources.

  The message reader takes an input source that is a function.
  The function must return successive characters from the source when called with 0 arguments.
  The function must support pushback of 1 character when called with 1 argument."
  (:import (java.io InputStream)))

(set! *warn-on-reflection* true)

(defn- pushback-fn [fun]
  (let [pushback (volatile! nil)]
     (fn
       ([]
        (if-let [val @pushback]
          (do
            (vreset! pushback nil)
            val)
          (fun)))
       ([pushback-val]
        (vreset! pushback pushback-val)))))


(defn input-stream
  "Returns an input stream source. Does not close input stream."
  [^InputStream in]
  (pushback-fn #(char (.read in))))
