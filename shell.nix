{ pkgs ? import (builtins.fetchTarball {
    url = "https://github.com/NixOS/nixpkgs/archive/2074cc672349f14240fb7ea90d509e0b643e9dd4.tar.gz";
    sha256 = "0x2cbp7qi17ll07dlns0bq6401r6563bvkv3bqvd6lia5ai5hj0k";
  }) {} }:

let
  libs = with pkgs; [
    gtk3 glib pango cairo gdk-pixbuf atk
    libx11 libxext libxrender libxtst libxi libxxf86vm
    mesa libGL
    fontconfig freetype
  ];
in
pkgs.mkShell {
  packages = with pkgs; [
    jdk21
  ] ++ libs;

  shellHook = ''
    export LD_LIBRARY_PATH="${pkgs.lib.makeLibraryPath libs}:$LD_LIBRARY_PATH"
  '';
  
}

