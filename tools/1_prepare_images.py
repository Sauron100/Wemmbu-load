import cv2, numpy as np
from PIL import Image
# name -> (src index, crop box l,t,r,b, inpaint rects [(x0,y0,x1,y1)])
spec = {
 'wemmbu_1_loading':   (1,(14,76,724,792), []),
 'wemmbu_2_transition':(2,(14,76,752,794), []),
 'wemmbu_3_title':     (3,(10,76,716,788), [(260,80,740,500)]),
 'wemmbu_4_menu_a':    (4,(10,76,718,790), [(300,80,760,520)]),
 'wemmbu_5_menu_b':    (5,(24,76,720,782), [(170,80,760,500)]),
 'manepear_1_loading': (7,(18,118,826,738), []),
 'manepear_2_transition':(6,(8,76,742,742), [(40,570,740,700)]),
 'manepear_3_title':   (9,(10,118,840,730), [(400,160,846,560)]),
 'manepear_4_menu':    (8,(18,76,840,742), [(380,215,846,520)]),
}
import os
os.makedirs('clean',exist_ok=True)
for name,(i,box,rects) in spec.items():
    im=cv2.imread(f'src/{i}.png')
    if rects:
        gray=cv2.cvtColor(im,cv2.COLOR_BGR2GRAY)
        k=cv2.getStructuringElement(cv2.MORPH_ELLIPSE,(31,31))
        th=cv2.morphologyEx(gray,cv2.MORPH_TOPHAT,k)
        m=np.zeros_like(gray)
        for x0,y0,x1,y1 in rects:
            sub=(th[y0:y1,x0:x1]>38).astype(np.uint8)*255
            m[y0:y1,x0:x1]=sub
        m=cv2.dilate(m,cv2.getStructuringElement(cv2.MORPH_ELLIPSE,(15,15)))
        im=cv2.inpaint(im,m,9,cv2.INPAINT_TELEA)
        for x0,y0,x1,y1 in rects:
            H,W=im.shape[:2]
            pad=30
            X0,Y0,X1,Y1=max(0,x0-pad),max(0,y0-pad),min(W,x1+pad),min(H,y1+pad)
            fm=np.zeros((H,W),np.float32); fm[Y0:Y1,X0:X1]=1
            fm=cv2.GaussianBlur(fm,(0,0),28)
            bl=cv2.GaussianBlur(im,(0,0),22).astype(np.float32)*0.62
            im=(im.astype(np.float32)*(1-fm[...,None])+bl*fm[...,None]).astype(np.uint8)
    l,t,r,b=box
    im=im[t:b,l:r]
    cv2.imwrite(f'clean/{name}.png',im)
    print(name,im.shape[1],im.shape[0])
