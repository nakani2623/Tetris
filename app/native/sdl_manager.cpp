#include "sdl_manager.h"
#include <thread>
#include <chrono>
#include <cstdio>
Sdl_manager::Sdl_manager(int width, int height) {
    SDL_Init(SDL_INIT_VIDEO);
    this->window = SDL_CreateWindow(
        "SDL window",
        width, height, 
        // SDL_WINDOW_RESIZABLE | 
        SDL_WINDOW_MOUSE_FOCUS | 
        SDL_WINDOW_TRANSPARENT | 
        SDL_WINDOW_FULLSCREEN
    );
    renderer = SDL_CreateRenderer(
        this->window, nullptr
    );
    SDL_RenderPresent(this->renderer);

}

void Sdl_manager::simple_window() {
    bool running = true;
    int B = 50;
    bool incre = true;
    SDL_Event event;

    while (running) {
        std::this_thread::sleep_for(std::chrono::milliseconds(30));

        while (SDL_PollEvent(&event)) {
            if (event.type == SDL_EVENT_QUIT) {
                running = false;
            }
            if (event.type == SDL_EVENT_QUIT) {
                running = false;
            }
        }

        // Clear screen
        SDL_SetRenderDrawColor(
            this->renderer, 0, 0, 0, 255
        );
        SDL_RenderClear(renderer);

        // Draw a red rectangle
        SDL_FRect rect = {200, (int)2*B, 50, 50};
        if (incre)
            B++;
        else
            B--;
        
        if (B >=255)
            incre = false;
        if (B <= 0)
            incre = true;
        SDL_SetWindowOpacity(this->window, (float)B / (float)255);
        // printf("opacity: %f\n", B/255);
        
        SDL_SetRenderDrawColor(
            this->renderer, 255, 0, B, 255
        );
        SDL_RenderFillRect(this->renderer, &rect);

        SDL_RenderPresent(this->renderer);
    }
}

Sdl_manager::~Sdl_manager() {
}