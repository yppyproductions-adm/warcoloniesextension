#!/usr/bin/env python3
from PIL import Image
import os

assets_dir = os.path.join('src','main','resources','assets','warcoloniesextension','textures','gui','modules')
files = ['tab_side1_padded.png','tab_side2_padded.png','tab_side3_padded.png','tab_side4_padded.png']

for fname in files:
    src = os.path.join(assets_dir, fname)
    if not os.path.exists(src):
        print(f"Missing: {src}")
        continue
    img = Image.open(src).convert('RGBA')
    w,h = img.size
    pixels = img.load()
    out = Image.new('RGBA', (w,h))
    out_pixels = out.load()

    for y in range(h):
        for x in range(w):
            if x==0:
                from_x = 1
            elif x==w-1:
                from_x = w-2
            else:
                from_x = x

            if y==0:
                from_y = 1
            elif y==h-1:
                from_y = h-2
            else:
                from_y = y

            out_pixels[x,y] = pixels[from_x, from_y]

    out_name = fname.replace('_padded','_filled')
    out_path = os.path.join(assets_dir, out_name)
    out.save(out_path)
    print(f"Wrote {out_path}")
