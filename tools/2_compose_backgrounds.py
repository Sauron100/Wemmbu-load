# Builds the 1600x900 background PNGs from tools/clean/*.png (output of 1_prepare_images.py)
import cv2, numpy as np, os, glob
W,H=1600,900
out='../src/main/resources/assets/characterloading/backgrounds'; os.makedirs(out,exist_ok=True)
for f in sorted(glob.glob('clean/*.png')):
    im=cv2.imread(f); h,w=im.shape[:2]
    s=max(W/w,H/h); bg=cv2.resize(im,(int(w*s)+1,int(h*s)+1),interpolation=cv2.INTER_CUBIC)
    y0=(bg.shape[0]-H)//2; x0=(bg.shape[1]-W)//2; bg=bg[y0:y0+H,x0:x0+W]
    bg=cv2.GaussianBlur(bg,(0,0),30).astype(np.float32)*0.55
    fw=int(w*H/h); fg=cv2.resize(im,(fw,H),interpolation=cv2.INTER_LANCZOS4).astype(np.float32)
    ramp=np.ones(fw,np.float32); r=min(90,fw//4); ramp[:r]=np.linspace(0,1,r); ramp[-r:]=np.linspace(1,0,r)
    ox=(W-fw)//2; c=bg.copy(); a=ramp[None,:,None]
    c[:,ox:ox+fw]=bg[:,ox:ox+fw]*(1-a)+fg*a
    cv2.imwrite(f'{out}/{os.path.basename(f)}',c.astype(np.uint8),[cv2.IMWRITE_PNG_COMPRESSION,9])
