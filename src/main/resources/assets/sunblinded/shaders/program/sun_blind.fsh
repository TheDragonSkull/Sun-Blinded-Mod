#version 150

uniform sampler2D DiffuseSampler;
uniform float Intensity;

in vec2 texCoord;
out vec4 fragColor;

void main() {
    vec4 color = texture(DiffuseSampler, texCoord);

    float gray = dot(color.rgb, vec3(0.299, 0.587, 0.114));

    vec3 mixed = mix(color.rgb, vec3(gray), Intensity);

    mixed += Intensity * 0.4;

    fragColor = vec4(clamp(mixed, 0.0, 1.0), color.a);
    //fragColor = vec4(Intensity, 0.0, 0.0, 1.0);
}
