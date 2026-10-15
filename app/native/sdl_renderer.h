#ifndef SDL_RENDERER_H
#define SDL_RENDERER_H
#include <SDL3/SDL.h>
#include "sdl_manager.h"

// #ifdef __cpluscplus
// extern "C" {
// #endif


extern "C" Sdl_manager *initSDL(int width, int height);
extern "C" void simple_window(Sdl_manager *sdl);
// extern "C" int poll_event(Sdl_manager *sdl);
extern "C" int poll_event();
// #ifdef __cpluscplus
// }
// #endif

#endif