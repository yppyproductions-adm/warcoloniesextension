from PIL import Image, ImageDraw, ImageFont
import os

# Paths (relative to repo root)
assets = 'src/main/resources/assets/warcoloniesextension/textures/gui'
modules = os.path.join(assets, 'modules')
builder = os.path.join(assets, 'builderhut')

out_dir = 'run/analysis'
os.makedirs(out_dir, exist_ok=True)

# Sizes from code
BG_W = 190
BG_H = 244

# Canvas include left tabs area (28 px)
LEFT_EXTRA = 28
canvas_w = BG_W + LEFT_EXTRA
canvas_h = BG_H

# Load background paper
paper_path = os.path.join(builder, 'builder_paper.png')
paper = Image.open(paper_path).convert('RGBA')

canvas = Image.new('RGBA', (canvas_w, canvas_h), (0,0,0,0))
# paste paper at x=LEFT_EXTRA
canvas.paste(paper, (LEFT_EXTRA, 0), paper)

draw = ImageDraw.Draw(canvas)
# font
try:
    font = ImageFont.truetype('/usr/share/fonts/truetype/dejavu/DejaVuSans.ttf', 12)
    font_bold = ImageFont.truetype('/usr/share/fonts/truetype/dejavu/DejaVuSans-Bold.ttf', 12)
except Exception:
    font = ImageFont.load_default()
    font_bold = font

# Draw sketches
sk_left = Image.open(os.path.join(builder, 'builder_sketch_left.png')).convert('RGBA')
sk_center = Image.open(os.path.join(builder, 'builder_sketch_center.png')).convert('RGBA')
sk_right = Image.open(os.path.join(builder, 'builder_sketch_right.png')).convert('RGBA')
canvas.paste(sk_left, (LEFT_EXTRA + 24, 12), sk_left)
canvas.paste(sk_center, (LEFT_EXTRA + 30, 12), sk_center)
canvas.paste(sk_right, (LEFT_EXTRA + 160, 12), sk_right)

# Draw title
title = 'Mesa do Arquiteto'
text_x = LEFT_EXTRA + 30
text_y = 14
# Pillow compatibility: use font.getsize
try:
    w = font_bold.getsize(title)[0]
except Exception:
    w = draw.textbbox((0,0), title, font=font_bold)[2]
draw.text((text_x + (128 - w)/2, text_y), title, font=font_bold, fill=(170,0,0,255))

# Assigned Workers label
label = 'Assigned Workers:'
try:
    label_w = font_bold.getsize(label)[0]
except Exception:
    label_w = draw.textbbox((0,0), label, font=font_bold)[2]
draw.text((LEFT_EXTRA + 13 + (164 - label_w)/2, 32), label, font=font_bold, fill=(0,0,0,255))

# Buttons - helper
def paste_button(img_name, x, y, text=None):
    b = Image.open(os.path.join(builder, img_name)).convert('RGBA')
    canvas.paste(b, (LEFT_EXTRA + x, y), b)
    if text:
        try:
            tw = font.getsize(text)[0]
        except Exception:
            tw = draw.textbbox((0,0), text, font=font)[2]
        tx = LEFT_EXTRA + x + (b.width - tw)//2
        ty = y + (b.height - 8)//2
        draw.text((tx, ty), text, font=font, fill=(0,0,0,255))

# Positions (from code)
# hire at left+30, top+74 size 129x17 -> use builder_button_medium_large.png (named builder_button_medium_large.png)
paste_button('builder_button_medium_large.png', 30, 74, 'PLACEHOLDER')
# recall 30,92
paste_button('builder_button_medium_large.png', 30, 92, 'PLACEHOLDER')
# build 30,110 (used earlier)
paste_button('builder_button_medium_large.png', 30, 110, 'PLACEHOLDER')
# deliveryPrioDown/up are mini buttons; draw small ones
mini = Image.open(os.path.join(builder, 'builder_button_mini.png')).convert('RGBA')
canvas.paste(mini, (LEFT_EXTRA + 127, 135), mini)
canvas.paste(mini, (LEFT_EXTRA + 144, 135), mini)
# draw +/- text
draw.text((LEFT_EXTRA + 127 + 4, 135 + 2), '-', font=font, fill=(0,0,0,255))
draw.text((LEFT_EXTRA + 144 + 3, 135 + 2), '+', font=font, fill=(0,0,0,255))
# inventory 52,214 size 86x17
paste_button('builder_button_medium.png', 52, 214, 'PLACEHOLDER')
# allinventory 159,214 icon
allinv_icon = Image.open(os.path.join(builder, 'builder_button_mini.png')).convert('RGBA')
canvas.paste(allinv_icon, (LEFT_EXTRA + 159, 214), allinv_icon)

# Lower block buttons (operate/sawmill/teach moved down by +4 in code)
paste_button('builder_button_medium_large.png', 30, 158, 'PLACEHOLDER')
paste_button('builder_button_medium_large.png', 30, 178, 'PLACEHOLDER')
paste_button('builder_button_medium_large.png', 30, 198, 'PLACEHOLDER')

# Middle worker list - just draw empty space
# draw delivery priority text
prio = f'Delivery Priority: 1'
draw.text((LEFT_EXTRA + 30, 135), prio, font=font, fill=(0,0,0,255))

# Tabs - tabX = left - 28 => on canvas x=0
tabX = 0
tabY = 24
# Load tab textures (use filled variants if present)
for i, t in enumerate(['tab_side1_filled.png','tab_side2_filled.png','tab_side3_filled.png','tab_side4_filled.png']):
    tp = Image.open(os.path.join(modules, t)).convert('RGBA')
    canvas.paste(tp, (tabX, tabY + i*28), tp)
    # module icon
    icon_name = ['inventory.png','stock.png','settings.png','info.png'][i]
    icon = Image.open(os.path.join(modules, icon_name)).convert('RGBA')
    canvas.paste(icon, (tabX + 7, tabY + 4 + i*28), icon)

# Decorative red wax icon bottom-left
wax = Image.open(os.path.join(builder, 'circle.png')).convert('RGBA')
canvas.paste(wax, (LEFT_EXTRA + 13, 214 - 20), wax)

out_path = os.path.join(out_dir, 'architect_gui.png')
canvas.save(out_path)
print('Wrote', out_path)
