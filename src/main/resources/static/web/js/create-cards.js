const app = Vue.createApp({
    data(){
        return{
            type: "CREDIT",
            color: "GOLD",
            urlApi:"http://localhost:8080/api/clients/current",
            usuario: "",
        }
    },
    created(){
        console.log(this.type)
        console.log(this.color)
        this.getdata()

    },
    methods:{
        getdata(){
            axios.get(this.urlApi)
            .then(data => {
                this.usuario = data.data.firstName + " " + data.data.lastName
                console.log(this.usuario)
            })},
        createCard(){
            Swal.fire({
                title: '¡Cuidado!',
                text: "¿Estás seguro de solicitar este tipo de tarjeta?",
                icon: 'warning',
                showCancelButton: true,
                confirmButtonColor: '#7b4aee',
                cancelButtonColor: '#d33',
                confirmButtonText: 'Si, solicitar tarjeta',
                cancelButtonText: 'Cancelar',
              }).then((result) => {
                if (result.isConfirmed) {
                    axios.post('/api/clients/current/cards',`type=${this.type}&color=${this.color}`)
                    .catch(function (error) {
                        let errorData = error.response.data
                        Swal.fire({
                            icon: 'error',
                            tittle: 'Oops...',
                            text: `${errorData}`
                      })})
                      Swal.fire({
                        title: 'Tarjeta solicitada con éxito',
                        icon: 'success',
                        text: '¿Quieres solicitar otra tarjeta?',
                        showDenyButton: true,
                        confirmButtonText: 'Si',
                        confirmButtonColor: '#7b4aee',
                        denyButtonText: `No, ir a mis tarjetas`,
                      }).then((result) => {
                        /* Read more about isConfirmed, isDenied below */
                        if (result.isConfirmed) {
                        } else if (result.isDenied) {
                            window.location.href = "/web/cards.html"
                        }
                      })
                }
              })
        },
        LogOut(){
            axios.post('/api/logout').then(response=> window.location.href = "/web/index.html")
        },
    },
})
app.mount("#app")