package com.gxnova.appgxnova.network;

import com.gxnova.appgxnova.models.AuthModels;
import com.gxnova.appgxnova.models.Categoria;
import com.gxnova.appgxnova.models.Notificacion;
import com.gxnova.appgxnova.models.PostulacionRequest;
import com.gxnova.appgxnova.models.Trabajo;
import com.gxnova.appgxnova.models.UsuarioSimple;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;

/**
 * Define todos los endpoints del backend GXNova.
 * BASE URL: https://backend-gxnova-production.up.railway.app/api/
 */
public interface ApiService {

        // ─── AUTH ─────────────────────────────────────────────────────────────────

        /** POST /api/auth/login */
        @POST("auth/login")
        Call<AuthModels.LoginResponse> login(@Body AuthModels.LoginRequest body);

        /**
         * POST /api/auth/register (multipart: foto_cedula, foto_perfil, selfie + campos
         * de texto)
         */
        @Multipart
        @POST("auth/register")
        Call<AuthModels.MessageResponse> register(
                        @Part("nombre") RequestBody nombre,
                        @Part("correo") RequestBody correo,
                        @Part("password") RequestBody password, // ← FIXED: era "contrasena"
                        @Part("telefono") RequestBody telefono,
                        @Part("rolNombre") RequestBody rolNombre, // ← backend usa rolNombre, no busqueda
                        @Part MultipartBody.Part fotoCedula,
                        @Part MultipartBody.Part selfie,
                        @Part MultipartBody.Part fotoPerfil);

        /** POST /api/auth/logout */
        @POST("auth/logout")
        Call<AuthModels.MessageResponse> logout(@Header("Authorization") String token);

        // ─── USUARIOS ─────────────────────────────────────────────────────────────

        /** GET /api/usuarios/perfil — Perfil del usuario autenticado */
        @GET("usuarios/perfil")
        Call<UsuarioSimple> getPerfil(@Header("Authorization") String token);

        /** PUT /api/usuarios/{id} — Actualizar perfil (con foto opcional) */
        @Multipart
        @PUT("usuarios/{id}")
        Call<UsuarioSimple> actualizarUsuario(
                        @Header("Authorization") String token,
                        @Path("id") int id,
                        @Part("nombre") RequestBody nombre,
                        @Part("telefono") RequestBody telefono,
                        @Part("ciudad") RequestBody ciudad,
                        @Part("descripcion") RequestBody descripcion,
                        @Part MultipartBody.Part fotoPerfil);

        /** GET /api/usuarios/{id}/perfil-publico */
        @GET("usuarios/{id}/perfil-publico")
        Call<UsuarioSimple> getPerfilPublico(@Path("id") int id);

        // ─── TRABAJOS ─────────────────────────────────────────────────────────────

        /** GET /api/trabajos — Listado público de trabajos */
        @GET("trabajos")
        Call<List<Trabajo>> getTrabajos();

        /** GET /api/trabajos/{id} */
        @GET("trabajos/{id}")
        Call<Trabajo> getTrabajoPorId(@Path("id") int id);

        /** POST /api/trabajos — Crear trabajo (Multipart, requiere auth) */
        @Multipart
        @POST("trabajos")
        Call<Trabajo> crearTrabajo(
                        @Header("Authorization") String token,
                        @Part("titulo") RequestBody titulo,
                        @Part("descripcion") RequestBody descripcion,
                        @Part("presupuesto") RequestBody presupuesto,
                        @Part("modalidad") RequestBody modalidad,
                        @Part("id_categoria") RequestBody idCategoria,
                        @Part MultipartBody.Part foto);

        /** DELETE /api/trabajos/{id} */
        @DELETE("trabajos/{id}")
        Call<AuthModels.MessageResponse> eliminarTrabajo(
                        @Header("Authorization") String token,
                        @Path("id") int id);

        // ─── CATEGORÍAS ───────────────────────────────────────────────────────────

        /** GET /api/categorias — Público */
        @GET("categorias")
        Call<List<Categoria>> getCategorias();

        // ─── POSTULACIONES ────────────────────────────────────────────────────────

        /** GET /api/postulaciones — Mis postulaciones */
        @GET("postulaciones")
        Call<List<Object>> getPostulaciones(@Header("Authorization") String token);

        /** POST /api/postulaciones */
        @POST("postulaciones")
        Call<AuthModels.MessageResponse> crearPostulacion(
                        @Header("Authorization") String token,
                        @Body PostulacionRequest body);

        /** PATCH /api/postulaciones/{id}/aceptar */
        @PATCH("postulaciones/{id}/aceptar")
        Call<AuthModels.MessageResponse> aceptarPostulacion(
                        @Header("Authorization") String token,
                        @Path("id") int id);

        /** PATCH /api/postulaciones/{id}/rechazar */
        @PATCH("postulaciones/{id}/rechazar")
        Call<AuthModels.MessageResponse> rechazarPostulacion(
                        @Header("Authorization") String token,
                        @Path("id") int id);

        // ─── NOTIFICACIONES ───────────────────────────────────────────────────────

        /** GET /api/notificaciones */
        @GET("notificaciones")
        Call<List<Notificacion>> getNotificaciones(@Header("Authorization") String token);

        /** GET /api/notificaciones/no-leidas */
        @GET("notificaciones/no-leidas")
        Call<List<Notificacion>> getNoLeidas(@Header("Authorization") String token);

        /** PATCH /api/notificaciones/{id}/leida */
        @PATCH("notificaciones/{id}/leida")
        Call<AuthModels.MessageResponse> marcarLeida(
                        @Header("Authorization") String token,
                        @Path("id") int id);

        /** PATCH /api/notificaciones/marcar-todas-leidas */
        @PATCH("notificaciones/marcar-todas-leidas")
        Call<AuthModels.MessageResponse> marcarTodasLeidas(@Header("Authorization") String token);

        // ─── CALIFICACIONES ───────────────────────────────────────────────────────

        /** GET /api/calificaciones/usuario/{id}/promedio */
        @GET("calificaciones/usuario/{id}/promedio")
        Call<Object> getPromedioUsuario(@Path("id") int id);
}
