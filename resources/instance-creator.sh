#!/bin/bash
for i in $(seq 1 $1)
do
   if test $(( $RANDOM % 100 + 1 )) -gt $2
   then
      zbctl --insecure create instance soundProcess --variables '{"incident": false}'
   else
      zbctl --insecure create instance soundProcess --variables '{"incident": true}'
   fi
done
