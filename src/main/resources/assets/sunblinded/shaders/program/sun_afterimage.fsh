#version 150

uniform sampler2D DiffuseSampler;
uniform sampler2D Prev;
uniform float Decay;

in vec2 texCoord;
out vec4 fragColor;

void main() {
    vec3 current = texture(DiffuseSampler, texCoord).rgb;
    vec3 previous = texture(Prev, texCoord).rgb;

    vec3 result = max(current, previous * Decay);

    fragColor = vec4(result, 1.0);
}
