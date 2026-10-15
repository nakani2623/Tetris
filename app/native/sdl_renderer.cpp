#include "sdl_renderer.h"
#include <cstdio>
extern "C" Sdl_manager *init_SDL(int width, int height) {
    // SDL_Init(SDL_INIT_VIDEO);
    // SDL_Window *window = SDL_CreateWindow(
    //     "SDL window",
    //     width, height, SDL_WINDOW_RESIZABLE | SDL_WINDOW_MOUSE_FOCUS | SDL_WINDOW_TRANSPARENT
    // );
    // SDL_Renderer *renderer = SDL_CreateRenderer(
    //     window, nullptr
    // );
    // SDL_SetWindowKeyboardGrab(window, false);
    Sdl_manager *sdl = new Sdl_manager(width, height);
    return sdl;
}

extern "C" void simple_window(Sdl_manager *sdl) {
    sdl->simple_window();

}

extern "C" int poll_event() {
    SDL_Event event;

    if (!SDL_PollEvent(&event)) {
        return 0; // No event
    }

    if (event.type == SDL_EVENT_QUIT) {
        return -1;
    }

    if (event.type == SDL_EVENT_KEY_DOWN) {
        if (event.key.repeat) {
            return 0;
        }

        switch (event.key.scancode) {
            case SDL_SCANCODE_LEFT:
                return 1;
            case SDL_SCANCODE_RIGHT:
                return 2;
            case SDL_SCANCODE_SPACE: {
                Uint64 now = SDL_GetTicksNS();

                double latencyMs =
                    (now - event.key.timestamp) / 1e6;

                printf("Input delay: %.3f ms\n", latencyMs);
                fflush(stdout);
                return 3;
            }
            case SDL_SCANCODE_Q:
                return 10;
            default:
                return 0;
        }
       
    }

    return 0;
}

// extern "C" int poll_event(Sdl_manager *sdl) {}

