#ifndef SDL_RENDERER_H
#define SDL_RENDERER_H
#include <SDL3/SDL.h>

#ifdef __cpluscplus
extern "C" {
#endif


SDL_Renderer *initSDL(int width, int height);
void simple_window(SDL_Renderer *renderer);

#ifdef __cpluscplus
}
#endif

#endif