#!/bin/sh

mkdir .git/hooks
ln -s ../../post-checkout.sh .git/hooks/post-checkout
chmod +x .git/hooks/post-checkout