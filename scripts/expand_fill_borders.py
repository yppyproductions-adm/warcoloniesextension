#!/usr/bin/env python3
from PIL import Image
import os

assets_dir = os.path.join('src','main','resources','assets','warcoloniesextension','textures','gui','modules')
files = ['tab_side1_filled.png','tab_side2_filled.png','tab_side3_filled.png','tab_side4_filled.png']
border = 4

for fname in files:
    src = os.path.join(assets_dir, fname)
    if not os.path.exists(src):
        print(f"Missing: {src}")
        continue
    img = Image.open(src).convert('RGBA')
    w,h = img.size
    new_w = w + border*2
    new_h = h + border*2
    out = Image.new('RGBA',(new_w,new_h))
    # paste center
    out.paste(img,(border,border))
    pixels = out.load()
    # fill left/right borders by copying nearest column
    for y in range(border, border+h):
        left_pixel = pixels[border, y]
        right_pixel = pixels[border + w -1, y]
        for x in range(0,border):
            pixels[x,y] = left_pixel
        for x in range(border+w, new_w):
            pixels[x,y] = right_pixel
    # fill top/bottom by copying nearest row
    for x in range(new_w):
        top_pixel = pixels[x, border]
        bottom_pixel = pixels[x, border + h -1]
        for y in range(0, border):
            pixels[x,y] = top_pixel
        for y in range(border+h, new_h):
            pixels[x,y] = bottom_pixel
    out_name = fname.replace('_filled','_filled4')
    out_path = os.path.join(assets_dir, out_name)
    out.save(out_path)
    print(f"Wrote {out_path}")
